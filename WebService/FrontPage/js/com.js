var InputZone = React.createClass({
	getInitialState: function(){
    	return {category:"游戏",limitDay:7,tag:""};
    },

    handleCategory:function(e){
    	var value = e.target.value;
    	this.setState({category:value});
    },

    handleLimitDay:function(e){
    	var value = e.target.value;
    	this.setState({limitDay:value});
    },

    handleTag:function(e){
    	var value = e.target.value;
    	this.setState({tag:value});
    },

    handleSearch:function(){
    	PubSub.publish("input",{category:this.state.category,limitDay:this.state.limitDay,tag:this.state.tag});
    },

    render:function(){
    	return (
    		<div>
    			<p>
    				<label>类型：</label>
    				<select  name='category' id='category' value={this.state.category} onChange={this.handleCategory}>
    					<option>请选择</option>
                                <option value="游戏">游戏</option>
                                <option value="动画">动画</option>
                                <option value="娱乐">娱乐</option>
                                <option value="广告">广告</option>
                                <option value="时尚">时尚</option>
                                <option value="生活">生活</option>
                                <option value="电影">电影</option>
                                <option value="电视剧">电视剧</option>
                                <option value="番剧">番剧</option>
                                <option value="科技">科技</option>
                                <option value="舞蹈">舞蹈</option>
                                <option value="音乐">音乐</option>
                        <option value="鬼畜">鬼畜</option>
                        <option value="公告">公告</option>
    				</select>
    			</p>
    			<p>
    				<label>最近天数：</label>
    				<input type="text" name="limitDay" value={this.state.limitDay} onChange={this.handleLimitDay} />
    			</p>
    			<p>
    				<label>关键字：</label>
    				<input type="text" name="tag" value={this.state.tag} onChange={this.handleTag} />
    			</p>
    			<p>
    				<button className="btn btn-default" onClick={this.handleSearch}>搜索</button>
    			</p>
    		</div>
    		);
    }
});

var ResourceList = React.createClass({
		getInitialState: function(){
    		return {listEle:[],page:1,searchParam:{category:"游戏",limitDay:7,tag:""}};
    	},

    	componentWillMount: function() {
    		var newState = this.state;
    		var url = "http://139.129.29.29/api/potentialResources?page="+this.state.page+"&category="+this.state.searchParam.category+"&limitDay="+this.state.searchParam.limitDay+"&tag="+this.state.searchParam.tag;
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

	  	handleClickNextPage: function() {
	  		var newState = this.state;
	  		newState.page+=1;

	  		var url = "http://139.129.29.29/api/potentialResources?page="+newState.page+"&category="+newState.searchParam.category+"&limitDay="+newState.searchParam.limitDay+"&tag="+newState.searchParam.tag;
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

	  	handleClickPrevPage: function() {
	  		var newState = this.state;
	  		if(newState.page <= 1){
	  			newState.page = 1;
	  			this.setState(newState);
	  			return;
	  		}

	  		newState.page-=1;

	  		var url = "http://139.129.29.29/api/potentialResources?page="+newState.page+"&category="+newState.searchParam.category+"&limitDay="+newState.searchParam.limitDay+"&tag="+newState.searchParam.tag;
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
    		this.pubsub_token = PubSub.subscribe('input', function(topic, dataValue) {
    			var newState = this.state;
    			newState.page = 1;
    			newState.listEle = [];
    			newState.searchParam = dataValue;

    			var url = "http://139.129.29.29/api/potentialResources?page="+newState.page+"&category="+newState.searchParam.category+"&limitDay="+newState.searchParam.limitDay+"&tag="+newState.searchParam.tag;
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
	  		var bodyHtml = [];
	  		this.state.listEle.forEach(function(e){
	  			var newDate = new Date();
	  			newDate.setTime(e.createDate);
	  			var dateStr = newDate.toLocaleDateString();
	  			var newDate2 = new Date();
	  			newDate2.setTime(e.updateDate);
	  			var dateStr2 = newDate2.toLocaleDateString();
	  			bodyHtml.push(
	  			<tr>
	                <td><img src={e.resourceShowUrl} /></td>
	                <td><a href={e.resourceUrl} target="_blank">{e.title}</a></td>
	                <td><a href={e.authorUrl}>{e.authorName}</a></td>
	                <td>{e.pv}</td>
	                <td>{e.commentNum}</td>
	                <td>{e.score}</td>
	                <td>{dateStr}</td>
	                <td>{dateStr2}</td>
                </tr>
                );
	  		});

	  		return (
	  		<div>
		  		<div className="container-fluid">
			  		<table className="table table-bordered">
			  		<tbody>
		            <tr>
		                <th>缩略图</th>
		                <th>标题</th>
		                <th>作者</th>
		                <th>点击量</th>
		                <th>评论量</th>
		                <th>分数</th>
		                <th>创建时间</th>
		                <th>最近更新</th>
		            </tr>
		            {bodyHtml}
		            </tbody>
		            </table>
	            </div>
	           	<p>
	           		<button className="btn btn-default" onClick={this.handleClickPrevPage}>上一页</button>
	            	<button className="btn btn-default" onClick={this.handleClickNextPage}>下一页</button>
	            </p>
            </div>
	  		);
	  	}
});

var NavTop = React.createClass({
	render:function(){
		return (
			<nav className="navbar navbar-default" role="navigation">
		        <a className="navbar-brand" href="#">资源排序</a>
		    </nav>
			);
	}
});

ReactDOM.render(<ResourceList />, document.getElementById("list"));
ReactDOM.render(<InputZone />, document.getElementById("input"));
ReactDOM.render(<NavTop />, document.getElementById("top"));