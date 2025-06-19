[#ftl]
[@b.head/]
[@b.grid items=promotionDefenseMembers var="promotionDefenseMember"]
  [@b.gridbar]
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
    bar.addItem("${b.text("action.export")}",action.exportData("group.name:组名,idx:组名顺序号,project.title:项目名称,project.manager.std.name:负责人",null,'fileName=项目答辩分组信息'));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col property="group.name" width="15%" title="组名"/]
    [@b.col property="idx" width="10%" title="组名顺序号"/]
    [@b.col width="20%" title="答辩时间" property="group.defenseOn"]
      ${promotionDefenseMember.group.defenseOn} ${promotionDefenseMember.group.beginAt}~${promotionDefenseMember.group.endAt}
    [/@]
    [@b.col title="项目名称" property="project.title"/]
    [@b.col width="15%" property="project.manager.std.name" title="负责人"]
      [#if promotionDefenseMember.project.members?size>1]
      <span title="参加${promotionDefenseMember.project.members?size}人">${(promotionDefenseMember.project.manager.std.name)!}<sup>${promotionDefenseMember.project.members?size}</sup></span>
      [#else]
      ${(promotionDefenseMember.project.manager.std.name)!}
      [/#if]
    [/@]
  [/@]
[/@]
[@b.foot/]
