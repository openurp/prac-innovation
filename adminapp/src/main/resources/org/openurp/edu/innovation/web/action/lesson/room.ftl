[#ftl]
{
rooms : [[#list rooms! as room]{id : '${room.id}', name : '${room.name?js_string}'}[#if room_has_next],[/#if][/#list]],
pageIndex : ${pageLimit.pageIndex},
pageSize : ${pageLimit.pageSize}
}
