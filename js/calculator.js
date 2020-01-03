var checkReg = /[A-z%#@!$><=]/;
var buttons = document.querySelectorAll("input");
for (var i = 0; i < buttons.length; i++) {
	buttons[i].addEventListener("click", clickBtn);
	if (buttons[i].value == "<"){
		buttons[i].addEventListener("dblclick", function() {
			let entry = document.getElementById("input-entry");
			entry.value = "";
			entry.style.color = "black";
		});
	}
		
}

let entry = document.getElementById("input-entry");

entry.oninput = function() {
	if (checkReg.test(entry.value)) {
	entry.style.color = "red";
	} else {
		entry.style.color = "black";
	}	
}


function clickBtn() {
	if (this.value != "" && this.type == "submit") {
		let entry = document.getElementById("input-entry");

		switch (this.value) {
			case "<":

				entry.value = entry.value.substring(0, entry.value.length - 1);
				break;
			case "=":
				entry.value = calculate(entry.value);
				break;
			default:
				entry.value += this.value;
				break;
		}
	}
}

function calculate(str) {
	if (checkReg.test(str)) {
		let entry = document.getElementById("input-entry");
		entry.style.color = "red";
		return str;
	}

	let stack = new Array();
	let result = 0;
	let expression = str.split(/([\*\/+-])/);
	let numbers = new Array();

	expression.forEach(function(letter, index, expression) {
		if (priority(letter) > 0) {
			if (stack.length < 1) {
				stack.push(letter);
			} else if (priority(stack[stack.length - 1]) >= priority(letter)) {
				stack.reverse().forEach(function(op, j, stack ) {
					operation(numbers, op);
				});
				stack = new Array();
				stack.push(letter);
			} else {
				stack.push(letter);
			}
		} else {
			numbers.push(parseFloat(letter));
		}
	});
	if (stack.length > 0) {
		stack.reverse().forEach(function(op, j, stack) {
			operation(numbers, op);
		});
	}
	result = numbers.shift();
	return result;
}

function operation(numbers, op) {
	let len = numbers.length;
	let num1 = 0;
	let num2 = 0;
	switch (op) {
		case "+":
			num1 = numbers.pop();
			num2 = numbers.pop();
			numbers.push(num2 + num1);
			break;
		case "*":
			num1 = numbers.pop();
			num2 = numbers.pop();
			numbers.push(num2 * num1);
			break;
		case "/":
			num1 = numbers.pop();
			num2 = numbers.pop();
			numbers.push(num2 / num1);
			break;
		case "-":
			num1 = numbers.pop();
			num2 = numbers.pop();
			numbers.push(num2 - num1);
			break;
		default:
			alert("Error op undefind: " + op);
	}
}

function priority(letter) {
	if (letter == "+" || letter == "-")
		return 1;
	else if (letter == "*" || letter == "/")
		return 2;
	else return 0;
}
