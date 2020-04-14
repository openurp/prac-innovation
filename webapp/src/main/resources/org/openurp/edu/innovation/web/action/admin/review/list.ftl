[#ftl]
[@b.head/]
[@b.grid items=reviews var="review"]
  [@b.gridbar]
    bar.addItem("退回",action.multi('clear',"确定清除内容和分数，退回专家评审?"))
    bar.addItem("${b.text("action.export")}",action.exportData("project.code:编号,project.title:项目名称,project.manager.std.user.name:负责人,project.department.name:部门,expert.code:评审专家,score:分数,level.name:推荐级别,comments:评审意见,project.reviewGroup.name:评审小组",null,'fileName=专家评审信息'));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="11%" property="project.code" title="编号"/]
    [@b.col width="32%" property="project.title" title="名称"]
      [@b.a href="!info?id="+review.id]<span style="font-size:0.8em">${review.project.title}</span>[/@]
    [/@]
    [@b.col width="8%" property="project.manager.std.user.name" title="负责人"]
      [#if review.project.members?size>1]
      <span title="拟参加${review.project.members?size}人">${(review.project.manager.std.user.name)!}<sup>${review.project.members?size}</sup></span>
      [#else]
      ${(review.project.manager.std.user.name)!}
      [/#if]
    [/@]
    [@b.col width="7%" property="department.name" title="学院"][#if review.project.department.shortName??]${review.project.department.shortName}[#else]${review.project.department.name}[/#if][/@]
    [@b.col width="7%" title="指导老师"][#list review.project.instructors as t]${t.user.name}[#sep],[/#sep][/#list][/@]
    [@b.col width="9%" property="project.discipline.name" title="学科"/]
    [@b.col width="7%" property="expert.name" title="评审专家"/]
    [@b.col width="7%" property="level.id" title="建议级别"]${(review.level.name)!'--'}[/@]
    [@b.col width="7%" property="score" title="评审成绩"/]
  [/@]
  [/@]
[@b.foot/]