var NavTop = React.createClass({
	render:function(){
		return (
			<nav className="navbar navbar-default" role="navigation">
		        <a className="navbar-brand" href="#">资源排序</a>
		    </nav>
			);
	}
});

var TopTopLayDiv = React.createClass({
	render:function(){
		return (
			<div className="container">
				<div className="row">
					<div className="span4">
					<FirstLayDiv className="row" category="游戏" />
					</div>
				</div>
				<br/>
			    <div className="row">
			    	<div className="span4">
					<FirstLayDiv className="row" category="动画" />
					</div>
				</div>
				<br/>
				<div className="row">
					<div className="span4">
					<FirstLayDiv className="row" category="娱乐" />
					</div>
				</div>
				<br/>
				<div className="row">
					<div className="span4">
					<FirstLayDiv className="row" category="时尚" />
					</div>
				</div>
				<br/>
				<div className="row">
					<div className="span4">
					<FirstLayDiv className="row" category="电影" />
					</div>
				</div>
				<br/>
				<div className="row">
					<div className="span4">
					<FirstLayDiv className="row" category="电视剧" />
					</div>
				</div>
				<br/>
				<div className="row">
					<div className="span4">
					<FirstLayDiv className="row" category="番剧" />
					</div>
				</div>
				<br/>
				<div className="row">
					<div className="span4">
					<FirstLayDiv className="row" category="科技" />
					</div>
				</div>
				<br/>
				<div className="row">
					<div className="span4">
					<FirstLayDiv className="row" category="音乐" />
					</div>
				</div>
			</div>
		);
	}
});


var FirstLayDiv = React.createClass({
	getInitialState: function(){
    	return {category:this.props.category};
    },

    render:function(){
		return (
			<div>
				<p><h3>{this.props.category}</h3></p>
				<div className="span2">
					<TwoLayInputDiv className="row" category={this.state.category} />
				</div>
				<br/>
				<div className="span2">
			    	<TwoLayContentDiv className="row" category={this.state.category} />
			    </div>
		    </div>
		);
	}
});

var TwoLayInputDiv = React.createClass({
	getInitialState: function(){
    	return {category:this.props.category,limitDay:7,tag:"",limitDayType:"近一周",page:1};
    },

    handleLimitDay:function(e){
    	var value = e.target.value;
    	this.setState({limitDay:value});
    },

    handleLimitDayType:function(e){
		var value = e.target.value;
    	if(value=="近一周"){
    		this.setState({limitDay:7,limitDayType:value});
    	}else if(value=="近三天"){
    		this.setState({limitDay:3,limitDayType:value});
    	}else if(value=="近一天"){
    		this.setState({limitDay:1,limitDayType:value});
    	}
    },

    handleTag:function(e){
    	var value = e.target.value;
    	this.setState({tag:value});
    },

    handleSearch:function(){
    	PubSub.publish("input"+this.state.category,{category:this.state.category,limitDay:this.state.limitDay,tag:this.state.tag,page:this.state.page});
    },

    handleNextPage:function(){
    	var page = this.state.page+1;
    	this.setState({page:page});
    	PubSub.publish("input"+this.state.category,{category:this.state.category,limitDay:this.state.limitDay,tag:this.state.tag,page:page});
    },

    handlePrePage:function(){
    	if(this.state.page>0){
    		var page = this.state.page-1;
	    	this.setState({page:page});
	    	PubSub.publish("input"+this.state.category,{category:this.state.category,limitDay:this.state.limitDay,tag:this.state.tag,page:page});
    	}
    },

    render:function(){
    	return (
    		<div className="row">
    			<div className="col-md-3">
    				<label>最近天数：</label>
    				<select  name='limitDayType' id='limitDayType' value={this.state.limitDayType} onChange={this.handleLimitDayType}>
    				<option value="近一周">近一周</option>
    				<option value="近三天">近三天</option>
    				<option value="近一天">近一天</option>
    				</select>
    			</div>
    			<div className="col-md-3">
    				<label>关键字：</label>
    				<input type="text" name="tag" value={this.state.tag} onChange={this.handleTag} />
    			</div>
    			<div className="col-md-1">
    				<button className="btn btn-default" onClick={this.handleSearch}>搜索</button>
    			</div>
    			<div className="col-md-1">
    				<button className="btn btn-default" onClick={this.handlePrePage}>上一页</button>
    			</div>
    			<div className="col-md-1">
    				<button className="btn btn-default" onClick={this.handleNextPage}>下一页</button>
    			</div>
    			<div className="col-md-1">
    				第{this.state.page}页
    			</div>
    		</div>
    	);
    }
});

var TwoLayContentDiv = React.createClass({
	getInitialState: function(){
    	return {listEle:[],searchParam:{category:this.props.category,limitDay:7,tag:"",page:1}};
    },

    componentWillMount: function() {
    	var newState = this.state;
    	var url = "http://139.129.29.29/api/potentialResources?page="+this.state.searchParam.page+"&category="+this.state.searchParam.category+"&limitDay="+this.state.searchParam.limitDay+"&tag="+this.state.searchParam.tag;
    	$.ajax({
    		context:this,
    		url:url,
    		success:function(result){
    			if(result.success == true){
    				newState.listEle=result.data.ele;
    				this.setState(newState);
    			}
    		}
    	});
    },

    componentDidMount: function(){
    	this.pubsub_token = PubSub.subscribe('input'+this.props.category, function(topic, dataValue) {
    		var newState = this.state;
    		newState.listEle = [];
    		newState.searchParam = dataValue;

    		var url = "http://139.129.29.29/api/potentialResources?page="+newState.searchParam.page+"&category="+newState.searchParam.category+"&limitDay="+newState.searchParam.limitDay+"&tag="+newState.searchParam.tag;
    		$.ajax({
    			context:this,
    			url:url,
    			success:function(result){
    				if(result.success == true){
    					newState.listEle=result.data.ele;
    					this.setState(newState);
    				}
    			}
    		});

    	}.bind(this));
    },

	render:function(){
		var bodyHtmlRow1 = [];
		var bodyHtmlRow2 = [];
		var bodyHtmlRow3 = [];
		var bodyHtmlRow4 = [];

		var i=0;
		this.state.listEle.forEach(function(e){
			var newDate = new Date();
			newDate.setTime(e.createDate);
			var dateStr = newDate.toLocaleDateString();
			var newDate2 = new Date();
			newDate2.setTime(e.updateDate);
			var dateStr2 = newDate2.toLocaleDateString();
			if(i<5){
				bodyHtmlRow1.push(
					<div className="col-md-2">
						<ThreeLayContentDiv className="row" rShowUrl={e.resourceShowUrl} rUrl={e.resourceUrl} rTitle={e.title} aUrl={e.authorUrl} aName={e.authorName} rScore={e.score} />
					</div>
				);
			}else if(i<10){
				bodyHtmlRow2.push(
					<div className="col-md-2">
						<ThreeLayContentDiv className="row" rShowUrl={e.resourceShowUrl} rUrl={e.resourceUrl} rTitle={e.title} aUrl={e.authorUrl} aName={e.authorName} rScore={e.score} />
					</div>
				);
			}else if(i<15){
				bodyHtmlRow3.push(
					<div className="col-md-2">
						<ThreeLayContentDiv className="row" rShowUrl={e.resourceShowUrl} rUrl={e.resourceUrl} rTitle={e.title} aUrl={e.authorUrl} aName={e.authorName} rScore={e.score} />
					</div>
				);
			}else if(i<20){
				bodyHtmlRow4.push(
					<div className="col-md-2">
						<ThreeLayContentDiv className="row" rShowUrl={e.resourceShowUrl} rUrl={e.resourceUrl} rTitle={e.title} aUrl={e.authorUrl} aName={e.authorName} rScore={e.score} />
					</div>
				);
			}
			i++;
		});
		return (
			<div>
				<div className="row">
					<div className="span2">
					{bodyHtmlRow1}
					</div>
				</div>
				<br/>
				<div className="row">
					<div className="span2">
					{bodyHtmlRow2}
					</div>
				</div>
				<br/>
				<div className="row">
					<div className="span2">
					{bodyHtmlRow3}
					</div>
				</div>
				<br/>
				<div className="row">
					<div className="span2">
					{bodyHtmlRow4}
					</div>
				</div>
			</div>
		);
	}
});

var ThreeLayContentDiv = React.createClass({
	render:function(){
		var resourceTitle = "";
		if(this.props.rTitle.length>=12){
			resourceTitle = this.props.rTitle.substr(0,12)+"...";
		}else{
			resourceTitle = this.props.rTitle;
		}
		return (
			<div>
				<div className="row">
					<a href={this.props.rUrl} target="_blank"><img src={this.props.rShowUrl} /></a>
				</div>
				<div className="row">
					<a href={this.props.rUrl} target="_blank">{resourceTitle}</a>
				</div>
				<div className="row">
					作者：<a href={this.props.aUrl}>{this.props.aName}</a>
				</div>
				<div className="row">
					分数：{this.props.rScore}
				</div>
			</div>
		);
	}
});

ReactDOM.render(<NavTop />, document.getElementById("top"));
ReactDOM.render(<TopTopLayDiv />, document.getElementById("list"));