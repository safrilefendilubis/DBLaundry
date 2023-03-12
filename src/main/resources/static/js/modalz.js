function funcHandler(event)
{
		var button = event.target
		var dataTitle = button.dataset.title
		var dataTarget = button.dataset.target
		var urlz = button.dataset.url
		$(dataTarget).on('show.bs.modal',function(){
			$.get(urlz, function (data) {
					$(dataTarget).find('.modal-body').html(data);
					$(dataTarget).find('.modal-title').text(dataTitle)
			});
		})

}