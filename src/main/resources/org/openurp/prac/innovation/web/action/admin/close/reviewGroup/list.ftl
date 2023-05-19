[#ftl]
[@b.head/]
[@b.grid items=reviewGroups var="reviewGroup"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="15%" property="name" title="组名"/]
    [@b.col title="成员"]
      [#list reviewGroup.experts as expert]${expert.name}(${expert.code})[#if expert_has_next],[/#if][/#list]
    [/@]
    [@b.col width="8%" title="项目数"]
      ${stats[reviewGroup.id?string]!}
    [/@]
    [@b.col width="12%" property="discipline.name" title="学科"/]
  [/@]
[/@]
[@b.foot/]
