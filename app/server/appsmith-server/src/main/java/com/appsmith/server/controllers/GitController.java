package com.appsmith.server.controllers;

import com.appsmith.external.dtos.GitLogDTO;
import com.appsmith.external.dtos.MergeStatusDTO;
import com.appsmith.external.dtos.GitStatusDTO;
import com.appsmith.server.constants.FieldName;
import com.appsmith.server.constants.Url;
import com.appsmith.server.domains.Application;
import com.appsmith.server.domains.GitApplicationMetadata;
import com.appsmith.server.domains.GitProfile;
import com.appsmith.external.dtos.GitBranchDTO;
import com.appsmith.server.dtos.GitCommitDTO;
import com.appsmith.server.dtos.GitConnectDTO;
import com.appsmith.server.dtos.GitMergeDTO;
import com.appsmith.server.dtos.GitPullDTO;
import com.appsmith.server.dtos.ResponseDTO;
import com.appsmith.server.services.GitService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(Url.GIT_URL)
public class GitController {

    private final GitService service;

    @Autowired
    public GitController(GitService service) {
        this.service = service;
    }

    @PostMapping("/profile/default")
    public Mono<ResponseDTO<Map<String, GitProfile>>> saveGitProfile(@RequestBody GitProfile gitProfile) {
        //Add to the userData object - git config data
        return service.updateOrCreateGitProfileForCurrentUser(gitProfile)
                .map(response -> new ResponseDTO<>(HttpStatus.OK.value(), response, null));
    }

    @PutMapping("/profile/{defaultApplicationId}")
    public Mono<ResponseDTO<Map<String, GitProfile>>> saveGitProfile(@PathVariable String defaultApplicationId,
                                                                     @RequestBody GitProfile gitProfile) {
        //Add to the userData object - git config data
        return service.updateOrCreateGitProfileForCurrentUser(gitProfile, Boolean.FALSE, defaultApplicationId)
                .map(response -> new ResponseDTO<>(HttpStatus.ACCEPTED.value(), response, null));
    }

    @GetMapping("/profile/default")
    public Mono<ResponseDTO<GitProfile>> getDefaultGitConfigForUser() {
        return service.getGitProfileForUser()
                .map(gitConfigResponse -> new ResponseDTO<>(HttpStatus.OK.value(), gitConfigResponse, null));
    }

    @GetMapping("/profile/{defaultApplicationId}")
    public Mono<ResponseDTO<GitProfile>> getGitConfigForUser(@PathVariable String defaultApplicationId) {
        return service.getGitProfileForUser(defaultApplicationId)
                .map(gitConfigResponse -> new ResponseDTO<>(HttpStatus.OK.value(), gitConfigResponse, null));
    }

    @GetMapping("/metadata/{defaultApplicationId}")
    public Mono<ResponseDTO<GitApplicationMetadata>> getGitMetadata(@PathVariable String defaultApplicationId) {
        return service.getGitApplicationMetadata(defaultApplicationId)
            .map(metadata -> new ResponseDTO<>(HttpStatus.OK.value(), metadata, null));
    }

    @PostMapping("/connect/{defaultApplicationId}")
    public Mono<ResponseDTO<Application>> connectApplicationToRemoteRepo(@PathVariable String defaultApplicationId,
                                                                         @RequestBody GitConnectDTO gitConnectDTO,
                                                                         @RequestHeader("Origin") String originHeader) {
        return service.connectApplicationToGit(defaultApplicationId, gitConnectDTO, originHeader)
                .map(application -> new ResponseDTO<>(HttpStatus.OK.value(), application, null));
    }

    @PostMapping("/commit/{defaultApplicationId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseDTO<String>> commit(@RequestBody GitCommitDTO commitDTO,
                                            @PathVariable String defaultApplicationId,
                                            @RequestHeader(name = FieldName.BRANCH_NAME, required = false) String branchName) {
        log.debug("Going to commit application {}, branch : {}", defaultApplicationId, branchName);
        return service.commitApplication(commitDTO, defaultApplicationId, branchName)
                .map(result -> new ResponseDTO<>(HttpStatus.CREATED.value(), result, null));
    }

    @GetMapping("/commit-history/{defaultApplicationId}")
    public Mono<ResponseDTO<List<GitLogDTO>>> getCommitHistory(@PathVariable String defaultApplicationId,
                                                               @RequestHeader(name = FieldName.BRANCH_NAME, required = false) String branchName) {
        log.debug("Fetching commit-history for application {}, branch : {}", defaultApplicationId, branchName);
        return service.getCommitHistory(defaultApplicationId, branchName)
                .map(logs -> new ResponseDTO<>(HttpStatus.OK.value(), logs, null));
    }

    @PostMapping("/push/{defaultApplicationId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseDTO<String>> push(@PathVariable String defaultApplicationId,
                                          @RequestHeader(name = FieldName.BRANCH_NAME, required = false) String branchName) {
        log.debug("Going to push application application {}, branch : {}", defaultApplicationId, branchName);
        return service.pushApplication(defaultApplicationId, branchName)
                .map(result -> new ResponseDTO<>(HttpStatus.CREATED.value(), result, null));
    }

    @PostMapping("/create-branch/{defaultApplicationId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseDTO<Application>> createBranch(@PathVariable String defaultApplicationId,
                                                       @RequestHeader(name = FieldName.BRANCH_NAME, required = false) String srcBranch,
                                                       @RequestBody GitBranchDTO branchDTO) {
        log.debug("Going to create a branch from root application {}, srcBranch {}", defaultApplicationId, srcBranch);
        return service.createBranch(defaultApplicationId, branchDTO, srcBranch)
                .map(result -> new ResponseDTO<>(HttpStatus.CREATED.value(), result, null));
    }

    @GetMapping("/checkout-branch/{defaultApplicationId}")
    public Mono<ResponseDTO<Application>> checkoutBranch(@PathVariable String defaultApplicationId,
                                                         @RequestHeader(name = FieldName.BRANCH_NAME, required = false) String branchName,
                                                         @RequestParam(required = false) Boolean isRemote) {
        log.debug("Going to checkout to branch {} application {} ", branchName, defaultApplicationId);
        return service.checkoutBranch(defaultApplicationId, branchName, isRemote)
            .map(result -> new ResponseDTO<>(HttpStatus.OK.value(), result, null));
    }

    @PostMapping("/disconnect/{defaultApplicationId}")
    public Mono<ResponseDTO<Application>> disconnectFromRemote(@PathVariable String defaultApplicationId) {
        log.debug("Going to remove the remoteUrl for application {}", defaultApplicationId);
        return service.detachRemote(defaultApplicationId)
                .map(result -> new ResponseDTO<>(HttpStatus.OK.value(), result, null));
    }

    @GetMapping("/pull/{defaultApplicationId}")
    public Mono<ResponseDTO<GitPullDTO>> pull(@PathVariable String defaultApplicationId,
                                          @RequestHeader(name = FieldName.BRANCH_NAME, required = false) String branchName) {
        log.debug("Going to pull the latest for application {}, branch {}", defaultApplicationId, branchName);
        return service.pullApplication(defaultApplicationId, branchName)
                .map(result -> new ResponseDTO<>(HttpStatus.OK.value(), result, null));
    }

    @GetMapping("/branch/{defaultApplicationId}")
    public Mono<ResponseDTO<List<GitBranchDTO>>> branch(@PathVariable String defaultApplicationId,
                                                            @RequestParam(required = false, defaultValue = "false") Boolean ignoreCache) {
        log.debug("Going to get branch list for application {}", defaultApplicationId);
        return service.listBranchForApplication(defaultApplicationId, BooleanUtils.isTrue(ignoreCache))
                .map(result -> new ResponseDTO<>(HttpStatus.OK.value(), result, null));
    }

    @GetMapping("/status/{defaultApplicationId}")
    public Mono<ResponseDTO<GitStatusDTO>> getStatus(@PathVariable String defaultApplicationId,
                                                     @RequestHeader(name = FieldName.BRANCH_NAME, required = false) String branchName) {
        log.debug("Going to get status for default application {}, branch {}", defaultApplicationId, branchName);
        return service.getStatus(defaultApplicationId, branchName)
                .map(result -> new ResponseDTO<>(HttpStatus.OK.value(), result, null));
    }

    @PostMapping("/merge/{defaultApplicationId}")
    public Mono<ResponseDTO<GitPullDTO>> merge(@PathVariable String defaultApplicationId,
                                               @RequestBody GitMergeDTO gitMergeDTO) {
        log.debug("Going to merge branch {} with branch {} for application {}", gitMergeDTO.getSourceBranch(), gitMergeDTO.getDestinationBranch(), defaultApplicationId);
        return service.mergeBranch(defaultApplicationId, gitMergeDTO)
                .map(result -> new ResponseDTO<>(HttpStatus.OK.value(), result, null));
    }

    @GetMapping("/merge/status/{defaultApplicationId}")
    public Mono<ResponseDTO<MergeStatusDTO>> mergeStatus(@PathVariable String defaultApplicationId,
                                                         @RequestParam MultiValueMap<String, String> params) {
        log.debug("Check if branch {} can be merged with branch {} for application {}", params.getFirst(FieldName.SOURCE_BRANCH), params.getFirst(FieldName.DESTINATION_BRANCH), defaultApplicationId);
        return service.isBranchMergeable(defaultApplicationId, params.getFirst(FieldName.SOURCE_BRANCH), params.getFirst(FieldName.DESTINATION_BRANCH))
                .map(result -> new ResponseDTO<>(HttpStatus.OK.value(), result, null));
    }

    @PostMapping("/conflicted-branch/{defaultApplicationId}")
    public Mono<ResponseDTO<String>> createConflictedBranch(@PathVariable String defaultApplicationId,
                                           @RequestHeader(name = FieldName.BRANCH_NAME) String branchName) {
        log.debug("Going to create conflicted state branch {} for application {}", branchName, defaultApplicationId);
        return service.createConflictedBranch(defaultApplicationId, branchName)
                .map(result -> new ResponseDTO<>(HttpStatus.OK.value(), result, null));
    }

}
