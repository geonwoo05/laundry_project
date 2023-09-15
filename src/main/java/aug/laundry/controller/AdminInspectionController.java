package aug.laundry.controller;

import aug.laundry.dto.AdminInspectionDto;
import aug.laundry.dto.Criteria;
import aug.laundry.dto.InspectionDataDto;
import aug.laundry.service.AdminInspectionService_ksh;
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

    @GetMapping("/admin/{adminId}")
    public String getInspectionView(@PathVariable("adminId") Long adminId) {
        return "project_manager_order_list";
    }
    @GetMapping("/admin/complete/{adminId}")
    public String getInspectedList(@PathVariable("adminId") Long adminId) {
        return "project_manager_order_list_complete";
    }
    @GetMapping("/getList/{pageNo}/{orderStatus}")
    public @ResponseBody Map<String, Object> getInspectionList(Model model, @PathVariable("pageNo") int pageNo
                                                ,@PathVariable("orderStatus") Long orderStatus) {
        Map<String, Object> inspectionMap = new HashMap<>();

        Criteria cri = new Criteria();
        cri.setPageNo(pageNo, adminInspectionService_ksh.getTotalCount(orderStatus));

        List<AdminInspectionDto> list = adminInspectionService_ksh.getInspectionList(cri, orderStatus);

        if(list != null) {
            inspectionMap.put("res", "success");
            inspectionMap.put("list", list);
            inspectionMap.put("realEnd", cri.getRealEnd());
        }else {
            inspectionMap.put("res", "fail");
        }
        return inspectionMap;
    }

    @GetMapping("/searchOrder/{ordersId}/{status}")
    public @ResponseBody Map<String, Object> getSaerchOrder(@PathVariable("ordersId") Long ordersId,
                                                            @PathVariable("status") Long status) {
        Map<String, Object> searchOrder = new HashMap<>();

        AdminInspectionDto orderInfo = adminInspectionService_ksh.getOrderSearchInfo(ordersId, status);
        if(orderInfo != null) {
            searchOrder.put("res", "success");
            searchOrder.put("orderInfo", orderInfo);
        }else {
            searchOrder.put("res", "fail");
        }
        return searchOrder;
    }

    @GetMapping("/admin/view/{adminId}/{ordersId}")
    public String viewInspectionDetail(@PathVariable("adminId") Long adminId,
                                      @PathVariable("ordersId") Long ordersId, Model model) {

        model.addAttribute("info", adminInspectionService_ksh.getInspectionDetail(ordersId));

        return "project_manager_order_detail";
    }

    @PostMapping("/admin/view/{adminId}/{ordersId}")
    public @ResponseBody Map<String, String> writeInspection( @PathVariable("adminId") Long adminId, @PathVariable("ordersId") Long ordersId,
                                                 @RequestPart("inspectionDataDto")  InspectionDataDto inspectionDataDto,
                                                 @RequestPart("file") List<MultipartFile> files) {
        Map<String, String> data = new HashMap<>();

        try {
            adminInspectionService_ksh.updateInspectionResult(adminId, ordersId, inspectionDataDto, files);
            data.put("result", "success");
        } catch (RuntimeException e) {
            data.put("result", "fail");
            data.put("error", e.getMessage());
        }
        return data;
    }

    @GetMapping("/admin/complete/view/{adminId}/{ordersId}")
    public String viewInspectionCompleteDetail(@PathVariable("adminId") Long adminId,
                                               @PathVariable("ordersId") Long ordersId, Model model) {

        model.addAttribute("info", adminInspectionService_ksh.getInspectionDetail(ordersId));

        return "project_manager_order_complete_detail";
    }

    @GetMapping("/admin/edit/view/{adminId}/{ordersId}")
    public String viewEditInspectionDetail(@PathVariable("adminId") Long adminId, @PathVariable("ordersId") Long ordersId,
                                       Model model) {

        model.addAttribute("info", adminInspectionService_ksh.getInspectionDetail(ordersId));
        return "project_manager_order_detail_edit";
    }

    @PostMapping("/admin/edit/view/{adminId}/{ordersId}")
    public @ResponseBody Map<String, String> editInspection(@PathVariable("adminId") Long adminId, @PathVariable("ordersId") Long ordersId,
                                                            @RequestPart("inspectionDataDto")  InspectionDataDto inspectionDataDto,
                                                            @RequestPart(name="file", required = false) List<MultipartFile> files) {
        Map<String, String> data = new HashMap<>();
        Map<String, String> result = adminInspectionService_ksh.deleteImageFile(inspectionDataDto.getDeleteFileList());

        try {
            adminInspectionService_ksh.updateInspectionResult(adminId, ordersId, inspectionDataDto, files);
            data.put("result", "success");
        } catch (RuntimeException e) {
            data.put("result", "fail");
            data.put("error", e.getMessage());
        }

//        if(result.isEmpty()) {
//            result.forEach((key, value) -> {
//                log.info("error={}", value);
//            });
//        }

        return data;
    }
}
