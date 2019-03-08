[#ftl]
[@b.head/]
[@b.grid items=projects var="project"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
    //bar.addItem("${b.text("action.export")}",action.exportData("code:代码,name:课程名称,enName:英文名,credits:学分,creditHours:学时,weekHours:周课时,department.name:所属部门,projectType.name:课程类型,examMode.name:考核方式,hasMakeup:是否有补考",null,'fileName=课程信息'));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="11%" property="code" title="编号"/]
    [@b.col width="35%" property="title" title="名称"][@b.a href="!info?id=${project.id}"]${project.title}[/@][/@]
    [@b.col width="8%" property="manager.std.user.name" title="负责人"]
      [#if project.members?size>1]
      <span title="拟参加${project.members?size}人">${(project.manager.std.user.name)!}<sup>${project.members?size}</sup></span>
      [#else]
      ${(project.manager.std.user.name)!}
      [/#if]
    [/@]
    [@b.col width="7%" property="level.name" title="级别"/]
    [@b.col width="8%" property="department.name" title="学院"][#if project.department.shortName??]${project.department.shortName}[#else]${project.department.name}[/#if][/@]
    [@b.col width="7%" title="指导老师"][#list project.instructors as t]${t.user.name}[#sep],[/#sep][/#list][/@]
    [@b.col width="9%" property="discipline.name" title="学科"/]
    [@b.col width="10%" property="state.name" title="状态"/]
  [/@]
  [/@]
[@b.foot/]
