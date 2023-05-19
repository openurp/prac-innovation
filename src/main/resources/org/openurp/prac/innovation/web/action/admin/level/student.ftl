[#ftl]
{
students : [[#list students! as student]{id : '${student.id}', name : '${student.name?js_string}', code : '${student.code?js_string}'}[#if student_has_next],[/#if][/#list]],
pageIndex : ${pageLimit.pageIndex},
pageSize : ${pageLimit.pageSize}
}
