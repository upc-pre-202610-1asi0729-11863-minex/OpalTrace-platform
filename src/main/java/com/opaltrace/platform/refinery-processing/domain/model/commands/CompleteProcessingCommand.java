package com.opaltrace.platform.refineryprocessing.domain.model.commands;

public record CompleteProcessingCommand(
        Long batchPk,
        Long supervisorId
) {}
