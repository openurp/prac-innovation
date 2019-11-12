[#ftl]
[@b.head/]
[@b.grid items=projects var="project"]
  [@b.gridbar]
    var menu= bar.addMenu("变更等级为..");
    [#list projectLevels as l]
    menu.addItem("${l.name}",action.multi("updateLevel","确定变更等级吗?","newLevel.id=${l.id}"));
    [/#list]
    bar.addItem("${b.text("action.export")}",action.exportData("",null,'fileName=项目信息&template=project.xls'));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="10%" property="code" title="编号"/]
    [@b.col width="33%" property="title" title="名称"]
      <span style="font-size:0.8em">
       [@b.a href="!info?id=${project.id}"]${project.title}[/@]
      </span>
    [/@]
    [@b.col width="7%" property="manager.std.user.name" title="负责人"]
      [#if project.members?size>1]
      <span title="拟参加${project.members?size}人">${(project.manager.std.user.name)!}<sup>${project.members?size}</sup></span>
      [#else]
      ${(project.manager.std.user.name)!}
      [/#if]
    [/@]
    [@b.col width="14%" title="获奖级别"]
    [#assign yearLevels={}/]
    [#list project.levels as l]
        [#assign existed = (yearLevels[l.year?string])![] /]
       [#assign yearLevels= yearLevels + {l.year?string:(existed+[l.level])}/]
    [/#list]

    [#list yearLevels  as y,v ]
      ${y} [#list v?sort_by("id") as l]${l.name}[#if l_has_next],[/#if][/#list] [#if y_has_next]<br>[/#if]
    [/#list]

    [/@]
    [@b.col width="7%" property="department.name" title="学院"][#if project.department.shortName??]${project.department.shortName}[#else]${project.department.name}[/#if][/@]
    [@b.col width="7%" title="指导老师"][#list project.instructors as t]${t.user.name}[#sep],[/#sep][/#list][/@]
    [@b.col width="7%" property="discipline.name" title="学科"/]
  [/@]
  [/@]
[@b.foot/]
