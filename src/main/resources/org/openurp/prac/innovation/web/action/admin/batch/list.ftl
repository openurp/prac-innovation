[#ftl]
[@b.head/]
[@b.grid items=batches var="batch"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="10%" property="name" title="名称"][@b.a href="!info?id=${batch.id}"]${batch.name}[/@][/@]
    [@b.col property="endOn" title="各阶段时间"]
    [#list batch.stages?sort_by(['stageType','indexno']) as stage]
    ${stage.stageType.name} [@displayDateRange stage.beginAt stage.endAt/]
    [/#list]
    [@b.col width="18%" property="beginOn" title="有效时间"]
      [@displayDateRange batch.beginOn batch.endOn/]
    [/@]
    [@b.col width="10%" property="archived" title="是否归档"]${batch.archived?string('是','否')}[/@]

    [/@]
  [/@]
[/@]

[#macro displayDateRange beginOn endOn]
  ${beginOn?string('yyyy-MM-dd')}~[#if endOn?string('yyyy')==beginOn?string('yyyy')]${endOn?string('MM-dd')}[#else]${endOn?string('yyyy-MM-dd')}[/#if]
[/#macro]
[@b.foot/]
