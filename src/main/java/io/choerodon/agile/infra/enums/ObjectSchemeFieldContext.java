package io.choerodon.agile.infra.enums;

import io.choerodon.core.exception.CommonException;

import java.util.Arrays;
import java.util.List;

/**
 * @author shinan.chen
 * @since 2019/3/29
 */
public class ObjectSchemeFieldContext {
    private ObjectSchemeFieldContext() {
    }

    public static final String GLOBAL = "global";

    public static final String STORY = "story";

    public static final String EPIC = "issue_epic";

    public static final String BUG = "bug";

    public static final String TASK = "task";

    public static final String SUB_TASK = "sub_task";

    public static final String FEATURE = "feature";

    public static final String[] CONTEXTS = {GLOBAL, STORY, EPIC, BUG, TASK, SUB_TASK, FEATURE};

    public static final String[] ISSUE_TYPES = {STORY, EPIC, BUG, TASK, SUB_TASK, FEATURE};

    public static final List<String> ISSUE_TYPES_LIST = Arrays.asList(ISSUE_TYPES);

    public static void isIllegalContexts(String[] context) {
        for (String str : context) {
            if (!Arrays.asList(CONTEXTS).contains(str)) {
                throw new CommonException("error.context.illegal");
            }
        }
    }

    public static void isIllegalIssueTypes(String[] context) {
        for (String str : context) {
            if (!Arrays.asList(ISSUE_TYPES).contains(str)) {
                throw new CommonException("error.context.illegal");
            }
        }
    }

    public static void isIllegalIssueType(String issueType) {
        if (!Arrays.asList(ISSUE_TYPES).contains(issueType)) {
            throw new CommonException("error.issue.type.illegal");
        }
    }

    public static boolean isGlobal(String[] context) {
        for (String str : context) {
            if (GLOBAL.equals(str)) {
                return true;
            }
        }
        return false;
    }
}
