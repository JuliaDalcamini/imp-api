package com.imp.checklist;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ChecklistService {
    public void createChecklist(ChecklistDTO request) {
    }

    public Checklist updateChecklist(ChecklistDTO requestWithNewInfo, String id) {
        return null;
    }

    public void deleteChecklist(String id) {
    }

    public Checklist getChecklistById(String id) {
        return null;
    }
}
