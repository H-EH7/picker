package picker.picker_backend.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import picker.picker_backend.post.model.dto.PostInsertDTO;
import picker.picker_backend.post.model.dto.PostSelectDTO;
import picker.picker_backend.post.model.dto.PostUpdateDTO;
import picker.picker_backend.post.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/{userId}")
    public List<PostSelectDTO> getPostById(@PathVariable String userId){

        return postService.getPostById(userId);
    }

    @PostMapping(value = "/insert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public int insertPost(@RequestParam String userId,
                          @RequestParam String postText,
                          @RequestParam(required = false) MultipartFile file){

        PostInsertDTO postInsertDTO = new PostInsertDTO(userId,postText);
        if(file != null && file.isEmpty()){
            String fileName = file.getOriginalFilename();
        }

        return postService.insertPost(postInsertDTO);
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public int updatePost(@RequestParam String userId,
                          @RequestParam String postText,
                          @RequestParam long postId,
                          @RequestParam(required = false) MultipartFile file){

        PostUpdateDTO postUpdateDTO = new PostUpdateDTO(userId,postText,postId);

        return postService.updatePost(postUpdateDTO);
    }
}
