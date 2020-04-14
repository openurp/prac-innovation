[#ftl]
{
teachers : [[#list teachers! as teacher]{id : '${teacher.id}', name : '${teacher.user.name?js_string}', code : '${teacher.user.code?js_string}'}[#if teacher_has_next],[/#if][/#list]],
pageIndex : ${pageLimit.pageIndex},
pageSize : ${pageLimit.pageSize}
}
