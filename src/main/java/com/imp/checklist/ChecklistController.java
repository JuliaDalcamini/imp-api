package com.imp.checklist;

import com.imp.appUser.AppUser;
import com.imp.appUser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/checklist")
public class ChecklistController {

    private ChecklistService checklistService;
    private AppUserService userService;

    @PostMapping("create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createChecklist(@RequestBody ChecklistDTO request) {
        checklistService.createChecklist(request);
    }

    @PutMapping("update")
    public ChecklistResponse updateChecklist(@RequestBody ChecklistDTO requestWithNewInfo, @RequestParam(name = "id") String id) {
        EmptyChecklist checklist = (EmptyChecklist) checklistService.updateChecklist(requestWithNewInfo, id);
        AppUser creator = userService.getUserById(checklist.getCreatorId());

        return ChecklistResponse.of(checklist, creator);
    }

    @DeleteMapping("delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteChecklist(@RequestParam(name = "id") String id) {
        checklistService.deleteChecklist(id);
    }

    @GetMapping("search")
    public ChecklistResponse getChecklist(@RequestParam(name = "id") String id) {
        EmptyChecklist checklist = (EmptyChecklist) checklistService.getChecklistById(id);
        AppUser creator = userService.getUserById(checklist.getCreatorId());

        return ChecklistResponse.of(checklist, creator);
    }
}
