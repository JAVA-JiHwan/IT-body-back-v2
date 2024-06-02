var eventModal = $('#eventModal');
var modalTitle = $('.modal-title');
var editTitle = $('#edit-title');
var editStart = $('#edit-start');
var editEnd = $('#edit-end');
var editColor = $('#edit-color');
var editDesc = $('#edit-desc');
var addBtnContainer = $('.modalBtnContainer-addEvent');
var modifyBtnContainer = $('.modalBtnContainer-modifyEvent');

/* ****************
 *  새로운 일정 생성
 * ************** */
var newEvent = function (start, end) {
    $("#contextMenu").hide(); //메뉴 숨김

    modalTitle.html('새로운 일정');
    editTitle.val('');
    editStart.val(start);
    editEnd.val(end);
    editDesc.val('');

    addBtnContainer.show();
    modifyBtnContainer.hide();
    eventModal.modal('show');

    $(document).ready(function () {
        $('#save-event').on('click', function () {
            var eventData = {
                title: $('#edit-title').val(),
                start: $('#edit-start').val(),
                end: $('#edit-end').val(),
                description: $('#edit-desc').val(),
                backgroundColor: $('#edit-color').val()
            };

            console.log('Event data:', eventData); // 데이터 구조 확인

            if (eventData.start > eventData.end) {
                alert('끝나는 날짜가 앞설 수 없습니다.');
                return false;
            }

            if (eventData.title === '') {
                alert('일정명은 필수입니다.');
                return false;
            }

            // 예약 데이터 객체 생성
            var reservationData = {
                title: eventData.title,
                startTime: eventData.start,
                endTime: eventData.end,
                description: eventData.description,
                color: eventData.backgroundColor,
                trainerId: 28,
                memberId: 38
            };

            console.log('Reservation data to be sent:', reservationData); // 데이터 구조 확인

            // 새로운 일정 저장
            $.ajax({
                type: 'POST',
                url: '/reserve/create',
                contentType: 'application/json',
                dataType: 'json',
                data: JSON.stringify(reservationData),
                success: function (response) {
                    console.log('Success response:', response);
                    $('#calendar').fullCalendar('removeEvents');
                    $('#calendar').fullCalendar('refetchEvents');
                    eventModal.modal('hide');
                },
                error: function (xhr, status, error) {
                    console.error('Error:', error);
                    console.log('Status:', status);
                    console.log('XHR:', xhr);
                    console.log('Response Text:', xhr.responseText);
                }
            });

            console.log('Sent reservation data as JSON:', JSON.stringify(reservationData));
        });
    });

};
