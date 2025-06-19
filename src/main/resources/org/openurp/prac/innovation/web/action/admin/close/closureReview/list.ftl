[#ftl]
[@b.head/]
[@b.grid items=reviews var="review"]
  [@b.gridbar]
    bar.addItem("初始化评审",action.method("generate"));
    var menu=bar.addMenu("分配评审小组..");
    [#list groups?sort_by('name') as group]
      menu.addItem("${group.name}[#if stats[group.id?string]??](${stats[group.id?string]})[/#if]",action.multi('assign',null,"&reviewGroup.id=${group.id}"));
    [/#list]
    bar.addItem("${b.text("action.export")}",action.exportData("project.code:编号,project.title:项目名称,project.manager.std.name:负责人,project.department.name:部门,score:评审分数,",null,'fileName=结项评审信息'));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="11%" property="project.code" title="编号"/]
    [@b.col property="project.title" title="名称"]<span style="font-size:0.8em">${review.project.title}</span>[/@]
    [@b.col width="8%" property="project.manager.std.name" title="负责人"]
      [#if review.project.members?size>1]
      <span title="拟参加${review.project.members?size}人">${(review.project.manager.std.name)!}<sup>${review.project.members?size}</sup></span>
      [#else]
      ${(review.project.manager.std.name)!}
      [/#if]
    [/@]
    [@b.col width="7%" property="project.department.name" title="学院"][#if review.project.department.shortName??]${review.project.department.shortName}[#else]${review.project.department.name}[/#if][/@]
    [@b.col width="7%" title="指导老师"][#list review.project.instructors as t]${t.name}[#sep],[/#sep][/#list][/@]
    [@b.col width="9%" property="project.discipline.name" title="学科"/]
    [@b.col width="7%" property="group.name" title="评审小组"/]
    [@b.col width="14%" property="score" title="评审成绩"/]
  [/@]
  [/@]
[@b.foot/]
