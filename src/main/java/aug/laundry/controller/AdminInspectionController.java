package aug.laundry.controller;

import aug.laundry.domain.CommonLaundry;
import aug.laundry.domain.Drycleaning;
import aug.laundry.domain.Repair;
import aug.laundry.dto.AdminInspectionDto;
import aug.laundry.dto.DrycleaningListDto;
import aug.laundry.dto.RepairListDto;
import aug.laundry.enums.fileUpload.FileUploadType;
import aug.laundry.service.AdminInspectionService_ksh;
import aug.laundry.service.FileUploadService_ksh;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AdminInspectionController {

    private final AdminInspectionService_ksh adminInspectionService_ksh;
    private final FileUploadService_ksh fileUploadService;

    @GetMapping("/admin")
    public String getInspectionList(Model model) {

        model.addAttribute("list", adminInspectionService_ksh.getInspectionList());

        return "/project_manager_order_list";
    }

    @GetMapping("/admin/complete")
    public String getInspectedList(Model model) {

        model.addAttribute("list", adminInspectionService_ksh.getInspectedList());

        return "/project_manager_order_list_complete";
    }

    @GetMapping("/admin/{ordersId}")
    public String getInspectionDetail(@PathVariable("ordersId") Long ordersId, Model model) {

        model.addAttribute("info", adminInspectionService_ksh.getInspectionDetail(ordersId));

        return "/project_manager_order_detail";
    }

    @PostMapping("/admin/write")
    public String writeInsepctionResult(AdminInspectionDto adminInfo,
                                        CommonLaundry commonLaundry,
                                        Long adminId,
                                        @ModelAttribute(value = "DrycleaningListDto") DrycleaningListDto drycleanings,
                                        @ModelAttribute(value = "RepairListDto") RepairListDto repairs,
                                        List<MultipartFile> files) {
        List<Drycleaning> list = drycleanings.getDrycleaningList();

        for (Drycleaning drycleaning : list) {
            log.info("drycleaning={}", drycleaning.getDrycleaningNotReason());
        }

        Map<String, Object> inspectionInsert = new HashMap<>();
        inspectionInsert.put("adminInfo", adminInfo);
        inspectionInsert.put("commonLaundry", commonLaundry);
        inspectionInsert.put("drycleanings", drycleanings);
        inspectionInsert.put("repairs", repairs);
        inspectionInsert.put("files", files);


        // 이미지업로드
        // fileUploadService.saveFile(files, adminInfo.getInspectionId(), FileUploadType.INSPECTION);

        return "redirect:/admin";
    }
}
