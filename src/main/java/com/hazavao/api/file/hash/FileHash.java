package com.hazavao.api.file.hash;

import com.hazavao.api.PojaGenerated;

@PojaGenerated
public record FileHash(FileHashAlgorithm algorithm, String value) {}
