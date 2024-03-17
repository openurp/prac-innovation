[#ftl]
[@b.head/]
[#include "../nav.ftl"/]
[#assign defaultBatch=batches?first/]
<div class="search-container">
    <div class="search-panel" >
    [@b.form name="promotionDefenseGroupSearchForm" action="!search" target="promotionDefenseGrouplist" title="ui.searchForm" theme="search"]
      [@b.select style="width:100px" name="promotionDefenseGroup.batch.id" label="立项年度" value=defaultBatch items=batches/]
      [@b.textfields names="promotionDefenseGroup.name;组名"/]
      <input type="hidden" name="orderBy" value="promotionDefenseGroup.name"/>
    [/@]
    </div>
    <div class="search-list">
      [@b.div id="promotionDefenseGrouplist" href="!search?orderBy=promotionDefenseGroup.name&promotionDefenseGroup.batch.id="+defaultBatch.id/]
    </div>
  </div>
[@b.foot/]
