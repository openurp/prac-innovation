[#ftl]
[@b.head/]
[@b.grid items=details var="detail"]
  [@b.gridbar]
    bar.addItem("退回",action.multi('clear',"确定清除内容和分数，退回专家评审?"))
    bar.addItem("${b.text("action.export")}",action.exportData("review.project.code:编号,review.project.title:项目名称,review.project.manager.std.name:负责人,review.project.department.name:院系,expert.code:评审专家,score:分数,passed:是否推荐立项,comments:评审意见,review.group.name:评审小组",null,'fileName=专家立项评审信息'));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="11%" property="review.project.code" title="编号"/]
    [@b.col property="review.project.title" title="名称"]
      [@b.a href="!info?id="+detail.id]<span style="font-size:0.8em">${detail.review.project.title}</span>[/@]
    [/@]
    [@b.col width="8%" property="review.project.manager.std.name" title="负责人"]
      [#if detail.review.project.members?size>1]
      <span title="拟参加${detail.review.project.members?size}人">${(detail.review.project.manager.std.name)!}<sup>${detail.review.project.members?size}</sup></span>
      [#else]
      ${(detail.review.project.manager.std.name)!}
      [/#if]
    [/@]
    [@b.col width="7%" property="review.project.department.name" title="院系"][#if detail.review.project.department.shortName??]${detail.review.project.department.shortName}[#else]${detail.review.project.department.name}[/#if][/@]
    [@b.col width="7%" title="指导老师"][#list detail.review.project.instructors as t]${t.name}[#sep],[/#sep][/#list][/@]
    [@b.col width="9%" property="review.project.discipline.name" title="学科"/]
    [@b.col width="7%" property="expert.name" title="评审专家"/]
    [@b.col width="7%" property="passed" title="推荐立项"]
      [#if detail.passed??]${detail.passed?string("推荐","不推荐")}[#else]--[/#if]
    [/@]
    [@b.col width="7%" property="score" title="评审成绩"/]
  [/@]
  [/@]
[@b.foot/]