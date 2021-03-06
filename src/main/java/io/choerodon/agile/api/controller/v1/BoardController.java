package io.choerodon.agile.api.controller.v1;

import com.alibaba.fastjson.JSONObject;
import io.choerodon.agile.api.vo.*;
import io.choerodon.agile.app.service.BoardService;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.choerodon.core.exception.CommonException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Created by HuangFuqiang@choerodon.io on 2018/5/14.
 * Email: fuqianghuang01@gmail.com
 */
@RestController
@RequestMapping(value = "/v1/projects/{project_id}/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Permission(level = ResourceLevel.ORGANIZATION)
    @ApiOperation("创建scrum board,创建默认列，关联项目状态")
    @PostMapping
    public ResponseEntity<Void> createScrumBoard(@ApiParam(value = "项目id", required = true)
                                           @PathVariable(name = "project_id") Long projectId,
                                           @ApiParam(value = "board name", required = true)
                                           @RequestParam String boardName) {
        boardService.create(projectId, boardName);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Permission(level = ResourceLevel.ORGANIZATION)
    @ApiOperation("更新scrum board")
    @PutMapping(value = "/{boardId}")
    public ResponseEntity<BoardVO> updateScrumBoard(@ApiParam(value = "项目id", required = true)
                                                     @PathVariable(name = "project_id") Long projectId,
                                                    @ApiParam(value = "agile board id", required = true)
                                                     @PathVariable @Encrypt Long boardId,
                                                    @ApiParam(value = "ScrumBoard对象", required = true)
                                                     @RequestBody BoardVO boardVO) {
        return Optional.ofNullable(boardService.update(projectId, boardId, boardVO))
                .map(result -> new ResponseEntity<>(result, HttpStatus.CREATED))
                .orElseThrow(() -> new CommonException("error.board.update"));
    }

    @Permission(level = ResourceLevel.ORGANIZATION)
    @ApiOperation("删除scrum board")
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteScrumBoard(@ApiParam(value = "项目id", required = true)
                                           @PathVariable(name = "project_id") Long projectId,
                                           @ApiParam(value = "agile board id", required = true)
                                           @PathVariable @Encrypt Long boardId) {
        boardService.delete(projectId, boardId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Permission(level = ResourceLevel.ORGANIZATION)
    @ApiOperation("根据id查询scrum board")
    @GetMapping(value = "/{boardId}")
    public ResponseEntity<BoardVO> queryScrumBoardById(@ApiParam(value = "项目id", required = true)
                                                        @PathVariable(name = "project_id") Long projectId,
                                                       @ApiParam(value = "agile board id", required = true)
                                                        @PathVariable @Encrypt Long boardId) {
        return Optional.ofNullable(boardService.queryScrumBoardById(projectId, boardId))
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElseThrow(() -> new CommonException("error.board.get"));
    }

    @Permission(level = ResourceLevel.ORGANIZATION)
    @ApiOperation("移动issue")
    @PostMapping(value = "/issue/{issueId}/move")
    public ResponseEntity<IssueMoveVO> move(@ApiParam(value = "项目id", required = true)
                                             @PathVariable(name = "project_id") Long projectId,
                                            @ApiParam(value = "issue id", required = true)
                                             @PathVariable @Encrypt Long issueId,
                                            @ApiParam(value = "转换id", required = true)
                                             @RequestParam @Encrypt Long transformId,
                                            @ApiParam(value = "issue move object", required = true)
                                             @RequestBody IssueMoveVO issueMoveVO) {
        return Optional.ofNullable(boardService.move(projectId, issueId, transformId, issueMoveVO, false))
                .map(result -> new ResponseEntity<>(result, HttpStatus.CREATED))
                .orElseThrow(() -> new CommonException("error.issue.update"));
    }


    @Permission(level = ResourceLevel.ORGANIZATION)
    @ApiOperation("根据projectId查询项目下的board")
    @GetMapping
    public ResponseEntity<List<BoardVO>> queryByProjectId(@ApiParam(value = "项目id", required = true)
                                                           @PathVariable(name = "project_id") Long projectId) {
        return Optional.ofNullable(boardService.queryByProjectId(projectId))
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElseThrow(() -> new CommonException("error.boardList.get"));
    }

    @Permission(level = ResourceLevel.ORGANIZATION)
    @ApiOperation("根据projectId查询项目下的用户的board设置")
    @GetMapping(value = "/user_setting/{boardId}")
    public ResponseEntity<UserSettingVO> queryUserSettingBoard(@ApiParam(value = "项目id", required = true)
                                                                @PathVariable(name = "project_id") Long projectId,
                                                               @ApiParam(value = "agile board id", required = true)
                                                                @PathVariable @Encrypt Long boardId) {
        return Optional.ofNullable(boardService.queryUserSettingBoard(projectId, boardId))
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElseThrow(() -> new CommonException("error.userSettingBoard.get"));
    }

    @Permission(level = ResourceLevel.ORGANIZATION)
    @ApiOperation("更新用户泳道设置")
    @PostMapping(value = "/user_setting/{boardId}")
    public ResponseEntity<UserSettingVO> updateUserSettingBoard(@ApiParam(value = "项目id", required = true)
                                                                 @PathVariable(name = "project_id") Long projectId,
                                                                @ApiParam(value = "agile board id", required = true)
                                                                 @PathVariable @Encrypt Long boardId,
                                                                @ApiParam(value = "swimlaneBasedCode", required = true)
                                                                 @RequestParam String swimlaneBasedCode) {
        return Optional.ofNullable(boardService.updateUserSettingBoard(projectId, boardId, swimlaneBasedCode))
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElseThrow(() -> new CommonException("error.userSettingBoard.update"));
    }

    @Permission(level = ResourceLevel.ORGANIZATION)
    @ApiOperation("all data , Refactoring")
    @PostMapping(value = "/{boardId}/all_data/{organization_id}")
    public ResponseEntity<JSONObject> queryByOptions(@ApiParam(value = "项目id", required = true)
                                                     @PathVariable(name = "project_id") Long projectId,
                                                     @ApiParam(value = "agile board id", required = true)
                                                     @PathVariable @Encrypt Long boardId,
                                                     @ApiParam(value = "组织id", required = true)
                                                     @PathVariable(name = "organization_id") Long organizationId,
                                                     @RequestBody BoardQueryVO boardQuery) {
        return Optional.ofNullable(boardService.queryAllData(projectId, boardId, organizationId, boardQuery))
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElseThrow(() -> new CommonException("error.board.get"));
    }

    @Permission(level = ResourceLevel.ORGANIZATION)
    @ApiOperation("校验看板名称重复性")
    @GetMapping("/check_name")
    public ResponseEntity<Boolean> checkName(@ApiParam(value = "项目id", required = true)
                                             @PathVariable(name = "project_id") Long projectId,
                                             @ApiParam(value = "board name", required = true)
                                             @RequestParam String boardName) {
        return Optional.ofNullable(boardService.checkName(projectId, boardName))
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElseThrow(() -> new CommonException("error.checkName.get"));

    }

}
