[#ftl]
[@b.head/]
[#include "../nav.ftl"/]
[#assign defaultBatch=batches?first/]
<div class="search-container">
    <div class="search-panel" >
    [@b.form name="promotionDefenseMemberSearchForm" action="!search" target="promotionDefenseMemberlist" title="ui.searchForm" theme="search"]
      [@b.select style="width:100px" name="promotionDefenseMember.group.batch.id" label="立项年度" value=defaultBatch items=batches/]
      [@b.textfields names="promotionDefenseMember.group.name;组名"/]
      [@b.textfields names="promotionDefenseMember.project.title;项目"/]
      [@b.textfields names="promotionDefenseMember.project.manager.std.name;负责人"/]
      <input type="hidden" name="orderBy" value="promotionDefenseMember.group.name,promotionDefenseMember.idx"/>
    [/@]
    </div>
    <div class="search-list">
      [@b.div id="promotionDefenseMemberlist" href="!search?orderBy=promotionDefenseMember.group.name,promotionDefenseMember.idx&promotionDefenseMember.group.batch.id="+defaultBatch.id/]
    </div>
  </div>
[@b.foot/]
