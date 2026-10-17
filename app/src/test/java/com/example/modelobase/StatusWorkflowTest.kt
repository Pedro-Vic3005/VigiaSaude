package com.example.modelobase

import com.example.modelobase.domain.OccurrenceStatus
import com.example.modelobase.domain.StatusWorkflow
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StatusWorkflowTest {
    @Test fun receivedCanOnlyMoveToTriage() {
        assertTrue(StatusWorkflow.canMove(OccurrenceStatus.RECEIVED, OccurrenceStatus.TRIAGE))
        assertFalse(StatusWorkflow.canMove(OccurrenceStatus.RECEIVED, OccurrenceStatus.FINISHED))
    }
    @Test fun statusMustNotChangeAfterFinish() {
        assertFalse(StatusWorkflow.canMove(OccurrenceStatus.FINISHED, OccurrenceStatus.TRIAGE))
    }
    @Test fun fieldValidationCanMoveToAttendance() {
        assertTrue(StatusWorkflow.canMove(OccurrenceStatus.WAITING_INSPECTION, OccurrenceStatus.ATTENDING))
    }
}
