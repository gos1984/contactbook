$(document).ready(function() {
    $('.fancybox').fancybox();

    $('.person-edit').click(function() {
        var personRow = $(this).parents('tr');
        personRow.find('td[data-person]').each(function(i, el) {
            $('#detail-person').find('input[name=' + $(el).attr("data-person") + ']').val($(el).text());
        });
    });

    $('.person-delete').click(function() {
        person.delete($(this).attr('data-id'));
    });

    $('.button_back').click(function() {
        history.back();
    });



});


var person = {
    edit: function(form) {
        var msg = $(form).serialize();
        $.post({
            url: '/person/edit/part',
            data: msg,
            success: function(data) {
                $.fancybox.close();
                $(form).find('input').val('');
                location.reload();
            },
            error:  function(xhr, str){
                alert('Возникла ошибка: ' + xhr.responseCode);
            }
        });
    },
    delete: function(id) {
        $.get({
            url: '/person/delete/' + id,
            success: function(data) {
                location.reload();
            },
            error:  function(xhr, str){
                alert('Возникла ошибка: ' + xhr.responseCode);
            }
        });
    }
}
