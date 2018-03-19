package com.HAYF.controller;

import com.HAYF.exception.ResourceNotFoundException;
import com.HAYF.model.PatientData;
import com.HAYF.repository.PatientDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PatientDataController {

    @Autowired
    PatientDataRepository patientDataRepository;

    // Get All PatientData
    @GetMapping("/patientdata")
    public List<PatientData> getAllPatientData() {
        return patientDataRepository.findAll();
    }

    // Create a new PatientData
    @PostMapping("/patientdata")
    public PatientData createPatientData(@Valid @RequestBody PatientData patientData) {
        return patientDataRepository.save(patientData);
    }

    // Get a Single PatientData
    @GetMapping("/patientdata/{id}")
    public PatientData getPatientDataById(@PathVariable(value = "id") Long patientDataId) {
        return patientDataRepository.findById(patientDataId)
                .orElseThrow(() -> new ResourceNotFoundException("PatientData", "id", patientDataId));
    }

    // Update a PatientData
    @PutMapping("/patientdata/{id}")
    public PatientData updatePatientData(@PathVariable(value = "id") Long patientDataId,
                           @Valid @RequestBody PatientData patientDataDetails) {

        PatientData patientData = patientDataRepository.findById(patientDataId)
                .orElseThrow(() -> new ResourceNotFoundException("PatientData", "id", patientDataId));

        patientData.setExtraInfo(patientDataDetails.getExtraInfo());
        patientData.setRating(patientDataDetails.getRating());
        patientData.setDoctorId(patientDataDetails.getDoctorId());
        patientData.setPatientId(patientDataDetails.getPatientId());

         return patientDataRepository.save(patientData);

    }
    // Delete a PatientData
    @DeleteMapping("/patientdata/{id}")
    public ResponseEntity<?> deletePatientData(@PathVariable(value = "id") Long patientDataId) {
        PatientData patientData = patientDataRepository.findById(patientDataId)
                .orElseThrow(() -> new ResourceNotFoundException("PatientData", "id", patientDataId));

        patientDataRepository.delete(patientData);

        return ResponseEntity.ok().build();
    }
}