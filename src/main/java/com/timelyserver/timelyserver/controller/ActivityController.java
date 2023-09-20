package com.timelyserver.timelyserver.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.timelyserver.timelyserver.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.timelyserver.timelyserver.entity.Activity;
import com.timelyserver.timelyserver.entity.TeamTask;
import com.timelyserver.timelyserver.entity.UserDetail;
import com.timelyserver.timelyserver.repository.ActivityRepository;
import com.timelyserver.timelyserver.repository.TeamTaskRepository;
import com.timelyserver.timelyserver.repository.UserRepository;

@RestController
@CrossOrigin(origins = "*")
public class ActivityController {

    @Value("${file.host}")
    private String host;
    private final String FOLDER_PATH = "/home/ubuntu/timely/timelyserver/src/main/java/com/timelyserver/timelyserver/files/";
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private TeamTaskRepository teamTaskRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/api/activity/{id}")
    List<Activity> getActivity(@PathVariable Long id) {
        List<Activity> arr = activityRepository.findUncompleteActivities(id);
        return activityRepository.findUncompleteActivities(id);
    }

    @GetMapping("/api/allactivity/{id}")
    List<Activity> getAllActivity(@PathVariable Long id) {

        return activityRepository.findAllByUserId(id);
    }

    @PutMapping("/api/activity/{id}/{email}")
    Optional<Activity> updateActivityStatus(@PathVariable Long id, @PathVariable String email) {

        return activityRepository.findById(id).map(activity -> {
            if (activity.getAssignedBy() != "self") {
                ArrayList<Long> ids = activity.getUserid();
                UserDetail user = userRepository.findUser(ids.get(0));
                teamTaskRepository.findTeamTask(activity.getAssignedBy(), activity.getDate(),
                        user.getEmail()).map(task -> {
                            Integer index = task.getMembers().indexOf(email);
                            ArrayList<String> status = task.getStatus();
                            status.set(index, "Completed");
                            task.setStatus(status);
                            return teamTaskRepository.save(task);
                        });

            }
            activity.setStatus("Completed");

            return activityRepository.save(activity);
        });
    }

    @PutMapping("/api/activity/{id}")
    Optional<Activity> updateActivity(@PathVariable Long id, @RequestBody Activity body) {
        System.out.println(id);
        return activityRepository.findById(id).map(activity -> {
            activity.setTitle(body.getTitle());
            activity.setContent(body.getContent());
            activity.setDate(body.getDate());

            return activityRepository.save(activity);
        });
    }

    @PostMapping("/api/activity")
    ResponseEntity<?> postActivity(@RequestParam Map<String, String> body, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        ArrayList<String> userIdList = new ArrayList<>(Arrays.asList(body.get("userid").substring(1, body.get("userid").length() - 1).split(",")));


        ArrayList<Long> userIdArrayList = new ArrayList<>();
        for (String userId : userIdList) {
            userIdArrayList.add(Long.parseLong(userId));
        }

        SimpleDateFormat originalDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat targetDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

        Date parsedDate = null;
        String formattedDateString;
        try {
            String dateValue = body.get("date");
            if (dateValue != null) {
                parsedDate = originalDateFormat.parse(dateValue);
            }
            formattedDateString = parsedDate != null ? targetDateFormat.format(parsedDate) : "";
        } catch (ParseException e) {
            // Handle parse exception
            formattedDateString = ""; // or any default value
        }
        String assignedBy = body.get("assignedBy");
        if (assignedBy == null || assignedBy.isEmpty()) {
            assignedBy = "self";
        }

 Activity newact = new Activity(body.get("title"),body.get("content"),userIdArrayList,parsedDate,assignedBy,body.get("file"),"self");



if(file != null) {
    java.io.File folder = new java.io.File(FOLDER_PATH + newact.getUserid().get(0));
    if (!folder.exists()) {

        folder.mkdirs();

    }
    Boolean aBoolean = FileUtil.uploadFileToFileSystem(file, newact.getTitle().replaceAll("\\s", "") + ".pdf", folder);


    String pdf_file = host + newact.getUserid().get(0) + "/" + newact.getTitle().replaceAll("\\s", "") + "/file";




    newact.setFile(pdf_file);
}




        Activity newactivity = activityRepository.save(newact);

        return ResponseEntity.ok(newactivity);

    }

    @GetMapping("/api/{userId}/{activityName}/file")
    public ResponseEntity<?> fetchPDF(@PathVariable Long userId, @PathVariable String activityName) throws IOException {
        String filePath = FOLDER_PATH + userId + "/" + activityName.replaceAll("\\s", "") + ".pdf";


        File file = new File(filePath);

        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        byte[] fileContent = Files.readAllBytes(Path.of(filePath));

        MediaType mediaType = MediaType.APPLICATION_PDF;
            return ResponseEntity.ok().contentType(mediaType).body(fileContent);



    }

    @PostMapping("/api/assignactivity")
    TeamTask assignActivity(@RequestParam Map<String, String> body, @RequestParam(value = "file", required = false) MultipartFile file) {
        ArrayList<String> userIdList = new ArrayList<>(Arrays.asList(body.get("userid").substring(1, body.get("userid").length() - 1).split(",")));


        ArrayList<Long> userIdArrayList = new ArrayList<>();
        for (String userId : userIdList) {
            userIdArrayList.add(Long.parseLong(userId));
        }

        SimpleDateFormat originalDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat targetDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

        Date parsedDate = null;
        String formattedDateString;
        try {
            String dateValue = body.get("date");
            if (dateValue != null) {
                parsedDate = originalDateFormat.parse(dateValue);
            }
            formattedDateString = parsedDate != null ? targetDateFormat.format(parsedDate) : "";
        } catch (ParseException e) {
            // Handle parse exception
            formattedDateString = ""; // or any default value
        }


        Activity newactivity = new Activity(body.get("title"),body.get("content"),userIdArrayList,parsedDate,body.get("assignedBy"),body.get("file"),body.get("type"));



        ArrayList<String> members = new ArrayList<>();
        ArrayList<String> status = new ArrayList<>();

        userIdArrayList.forEach(id -> {
            UserDetail user = userRepository.findUser(id);
            user.getEmail();
            members.add(user.getEmail());
            status.add("Not Completed");



            ArrayList<Long> idArray = new ArrayList<>();
            idArray.add(0, id);
            newactivity.setUserid(idArray);
            if(file != null) {
                java.io.File folder = new java.io.File(FOLDER_PATH + newactivity.getUserid().get(0));
                if (!folder.exists()) {

                    folder.mkdirs();

                }
                try {
                    Boolean aBoolean = FileUtil.uploadFileToFileSystem(file, newactivity.getTitle().replaceAll("\\s", "") + ".pdf", folder);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                String pdf_file = host + newactivity.getUserid().get(0) + "/" + newactivity.getTitle().replaceAll("\\s", "") + "/file";




                newactivity.setFile(pdf_file);
            }
            newactivity.setAssignedBy(newactivity.getAssignedBy());
            activityRepository.save(newactivity);

        });
        TeamTask task = new TeamTask();
        String pdf_file = host + newactivity.getUserid().get(0) + "/" + newactivity.getTitle().replaceAll("\\s", "") + "/file";
        task.setTitle(newactivity.getTitle());
        task.setContent(newactivity.getContent());
        task.setDate(newactivity.getDate());
        task.setAssignedby(newactivity.getAssignedBy());
        task.setMembers(members);
        task.setStatus(status);
        task.setFile(pdf_file);
        return teamTaskRepository.save(task);
    }

    @DeleteMapping("/api/activity/{id}")
    Optional<Activity> deleteActivity(@PathVariable Long id) {
        Optional<Activity> deleted = activityRepository.findById(id);
        activityRepository.deleteById(id);
        return deleted;
    }

    @PutMapping("/api/activitypin/{userid}/{activity_id}")
    ResponseEntity<?> pinActivity(@PathVariable Long userid, @PathVariable Long activity_id) {

        Integer isAllPinned = activityRepository.checkPinned(userid);
        System.out.println(isAllPinned);

        System.out.println(activity_id);
        if (isAllPinned == 3) {

            return ResponseEntity.badRequest().body("Maximum pin reached");

        } else {
            Optional<Activity> activity = activityRepository.findById(activity_id).map(newactivity -> {
                newactivity.setPin(true);

                return activityRepository.save(newactivity);
            });
            return ResponseEntity.ok(activity);
        }

    }




    @PutMapping("/api/activityunpin/{activity_id}")
    Optional<Activity> unpinActivity(@PathVariable Long activity_id) {

        return activityRepository.findById(activity_id).map(newactivity -> {
            newactivity.setPin(false);

            return activityRepository.save(newactivity);
        });

    }

}
