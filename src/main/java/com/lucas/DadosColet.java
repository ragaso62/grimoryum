package com.lucas;

import com.sun.management.OperatingSystemMXBean;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class DadosColet {
    private final OperatingSystemMXBean os =
            (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    public  double cpuPercent() {
        double v = os.getProcessCpuLoad();
        if (v < 0) return  0;
        return v * Runtime.getRuntime().availableProcessors() * 100;
    }
    public int ramUsadaMb() {
        Runtime r = Runtime.getRuntime();
        return (int) ((r.totalMemory() - r.freeMemory()) / 1_048_576);
    }
    public  int ramMaxMb() {
        return (int) (Runtime.getRuntime().maxMemory() / 1_048_576);
    }

    public long tempoAtivoSeg(){
        return ManagementFactory.getRuntimeMXBean().getUptime() / 1000;
    }

    private final double discoTotalGb;
    private volatile double discoUsadoGb = 0;

    public DadosColet(double discoTotalGb) {
        this.discoTotalGb = discoTotalGb;
    }

    public double discoTotalGb() {
        return discoTotalGb;
    }

    public double discoUsadoGb() {
        return discoUsadoGb;
    }

    public void atualizarDisco() {
        Path raiz = Path.of(".").toAbsolutePath().normalize();
        long[] soma = {0};
        try {
            Files.walkFileTree(raiz, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path f, BasicFileAttributes a) {
                    soma[0] += a.size();
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path f, IOException e) {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException ignored) {
        }
        discoUsadoGb = soma[0] / 1_073_741_824.0;
    }
}
