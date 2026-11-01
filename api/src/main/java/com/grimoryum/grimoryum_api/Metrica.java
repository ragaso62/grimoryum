package com.grimoryum.grimoryum_api;
import java.time.Instant;

public record Metrica (
    Instant coletadoEm,
    double cpuPercent,
    int ramUsadaMb,
    int ramMaxMb,
    double discoUsadoGb,
    double discoTotalGb,
    long redeEntradaBps,
    long redeSaidaBps,
    double tps,
    int playersOnline,
    long tempoAtivoSeg
) {}
