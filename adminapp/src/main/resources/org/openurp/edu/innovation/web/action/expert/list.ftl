[#ftl]
[@b.head/]
[@b.grid items=experts var="expert"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="15%" property="name" title="名称"][@b.a href="!info?id=${expert.id}"]${expert.name}[/@][/@]
    [@b.col width="15%" property="code" title="账户"/]
    [@b.col width="50%" property="intro" title="介绍"/]
    [@b.col width="15%" property="discipline.name" title="学科"/]
  [/@]
[/@]
[@b.foot/]
