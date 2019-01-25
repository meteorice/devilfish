$(window, document, undefined).ready(function () {
	particlesJS("particles-js", {
		"particles": {
			"number": {
				"value": 46, "density": { "enable": true, "value_area": 1420.4657549380909 }
			},
			"color": {
				"value": "#9BA7B6"
			},
			"shape": {
				"type": "triangle",
				"stroke": {
					"width": 0, "color": "#9BA7B6"
				},
				"polygon": {
					"nb_sides": 5
				},
				"image": { "src": "img/github.svg", "width": 100, "height": 100 }
			}, "opacity": {
				"value": 0.40, "random": false, "anim": { "enable": false, "speed": 1, "opacity_min": 0.1, "sync": false }
			}, "size": {
				"value": 11.83721462448409,
				"random": true,
				"anim": { "enable": false, "speed": 40, "size_min": 0.1, "sync": false }
			},
			"line_linked": { "enable": true, "distance": 150, "color": "#9BA7B6", "opacity": 0.8, "width": 1 },
			"move": {
				"enable": true, "speed": 6, "direction": "none", "random": false, "straight": false, "out_mode": "out", "bounce": false,
				"attract": { "enable": false, "rotateX": 600, "rotateY": 1200 }
			}
		}, "interactivity": {
			"detect_on": "canvas", "events": {
				"onhover": {
					"enable": true, "mode": "repulse"
				},
				"onclick": {
					"enable": true, "mode": "push"
				}, "resize": true
			}, "modes": {
				"grab": {
					"distance": 400, "line_linked": { "opacity": 1 }
				},
				"bubble": {
					"distance": 400, "size": 40, "duration": 2, "opacity": 8, "speed": 3
				},
				"repulse": {
					"distance": 200, "duration": 0.4
				}, "push": {
					"particles_nb": 4
				}, "remove": {
					"particles_nb": 2
				}
			}
		}, "retina_detect": true
	});

	if($('input[name="username"]').val() != ""){
		$('input[name="username"]').addClass('used');
	}

	if($('input[name="password"]').val() != ""){
		$('input[name="password"]').addClass('used');
	}


	$('input').blur(function () {
		var $this = $(this);
		if ($this.val())
			$this.addClass('used');
		else
			$this.removeClass('used');
	});

	var $ripples = $('.ripples');

	$ripples.on('click.Ripples', function (e) {

		var $this = $(this);
		var $offset = $this.parent().offset();
		var $circle = $this.find('.ripplesCircle');

		var x = e.pageX - $offset.left;
		var y = e.pageY - $offset.top;

		$circle.css({
			top: y + 'px',
			left: x + 'px'
		});

		$this.addClass('is-active');

	});

	$ripples.on('animationend webkitAnimationEnd mozAnimationEnd oanimationend MSAnimationEnd', function (e) {
		$(this).removeClass('is-active');
	});

});


window.particlesJS.load = function (tag_id, path_config_json, callback) {
	var xhr = new XMLHttpRequest();
	xhr.open('GET', path_config_json);
	xhr.onreadystatechange = function (data) {
		if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				var params = JSON.parse(data.currentTarget.response);
				window.particlesJS(tag_id, params);
				if (callback) callback();
			} else {
				console.log('Error pJS - XMLHttpRequest status: ' + xhr.status);
				console.log('Error pJS - File config not found');
			}
		}
	};
	xhr.send();
};