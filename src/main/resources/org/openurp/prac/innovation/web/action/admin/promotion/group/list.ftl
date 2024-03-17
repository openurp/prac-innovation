[#ftl]
[@b.head/]
[@b.grid items=promotionDefenseGroups var="promotionDefenseGroup"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col property="name" title="组名"/]
    [@b.col width="20%" title="答辩时间" property="defenseOn"]
      ${promotionDefenseGroup.defenseOn} ${promotionDefenseGroup.beginAt}~${promotionDefenseGroup.endAt}
    [/@]
    [@b.col width="25%" title="答辩地点" property="location"/]
    [@b.col width="10%" title="计划项目数" property="capacity"/]
    [@b.col width="10%" title="实际项目数"]
      ${promotionDefenseGroup.members?size}
    [/@]
  [/@]
[/@]
[@b.foot/]
