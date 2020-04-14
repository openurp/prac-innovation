[#ftl]
[@b.head/]
[#include "../review_nav.ftl"/]
[#assign defaultBatch=batches?first/]
<table class="indexpanel">
  <tr>
    <td class="index_view" >
    [@b.form name="reviewGroupSearchForm" action="!search" target="reviewGrouplist" title="ui.searchForm" theme="search"]
      [@b.select style="width:100px" name="reviewGroup.batch.id" label="年度批次" value=defaultBatch items=batches/]
      [@b.textfields names="reviewGroup.name;组名"/]
      <input type="hidden" name="orderBy" value="name desc"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="reviewGrouplist" href="!search?orderBy=name&reviewGroup.batch.id="+defaultBatch.id/]
    </td>
  </tr>
</table>
[@b.foot/]
