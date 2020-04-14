[#ftl]
[@b.head/]
[@b.grid items=closures var="closure"]
  [@b.gridbar]
    var menu=bar.addMenu("分配评审小组..");
    [#list groups?sort_by('name') as group]
      menu.addItem("${group.name}[#if stats[group.id?string]??](${stats[group.id?string]})[/#if]",action.multi('assign',null,"&reviewGroup.id=${group.id}"));
    [/#list]
    bar.addItem("${b.text("action.export")}",action.exportData("project.code:编号,project.title:项目名称,project.manager.std.user.name:负责人,project.department.name:部门,applyExemptionReply:申请免答辩,exemptionReason:免答辩理由,exemptionConfirmed:免答辩审核通过,replyScore:答辩成绩",null,'fileName=结项信息'));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="11%" property="project.code" title="编号"/]
    [@b.col width="32%" property="project.title" title="名称"]<span style="font-size:0.8em">${closure.project.title}</span>[/@]
    [@b.col width="8%" property="project.manager.std.user.name" title="负责人"]
      [#if closure.project.members?size>1]
      <span title="拟参加${closure.project.members?size}人">${(closure.project.manager.std.user.name)!}<sup>${closure.project.members?size}</sup></span>
      [#else]
      ${(closure.project.manager.std.user.name)!}
      [/#if]
    [/@]
    [@b.col width="7%" property="department.name" title="学院"][#if closure.project.department.shortName??]${closure.project.department.shortName}[#else]${closure.project.department.name}[/#if][/@]
    [@b.col width="7%" title="指导老师"][#list closure.project.instructors as t]${t.user.name}[#sep],[/#sep][/#list][/@]
    [@b.col width="9%" property="project.discipline.name" title="学科"/]
    [@b.col width="7%" property="project.reviewGroup.name" title="评审小组"/]
    [@b.col width="14%" property="project.reviewScore" title="评审成绩"/]
  [/@]
  [/@]
[@b.foot/]
