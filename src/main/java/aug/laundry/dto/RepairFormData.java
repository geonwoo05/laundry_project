package aug.laundry.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RepairFormData {

    private String title;
    private String request;
    private List<MultipartFile> files;

    public RepairFormData(String title, String request, List<MultipartFile> files) {
        this.title = title;
        this.request = request;
        this.files = files;
    }
}
