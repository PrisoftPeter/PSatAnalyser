Creator	"yFiles"
graph
[
	hierarchic	1
	label	""
	directed	1
	node
	[
		id	Start
		label	"Start"
	]
	node
	[
		id	Consent1
		label	"Consent1"
	]
	node
	[
		id	Sent1
		label	"Sent1"
	]
	node
	[
		id	End
		label	"End"
	]
	node
	[
		id	Sent2
		label	"Sent2"
	]
	node
	[
		id	Consent2
		label	"Consent2"
	]
	node
	[
		id	Consent3
		label	"Consent3"
	]
	node
	[
		id	Consent4
		label	"Consent4"
	]	
	node
	[
		id	Notice1-su
		label	"Notice1-su"
	]
	node
	[
		id	Notice2-su
		label	"Notice2-su"
	]
	node
	[
		id	Notice1-r
		label	"Notice1-r"
	]
	node
	[
		id	Notice2-r
		label	"Notice2-r"
	]
	node
	[
		id	Request
		label	"Request"
	]
	edge
	[
		source	Start
		target	Consent1
	]
	edge
	[
		source	Start
		target	Consent2
	]
	edge
	[
		source	Start
		target	Consent3
	]
	edge
	[
		source	Start
		target	Consent4
	]
	edge
	[
		source	Consent1
		target	Sent1
	]
	edge
	[
		source	Sent1
		target	End
	]
	edge
	[
		source	Start
		target	Request
	]
	edge
	[
		source	Start
		target	Sent1
	]
	edge
	[
		source	Start
		target	Sent2
	]
	edge
	[
		source	Request
		target	Consent1
	]
	edge
	[
		source	Request
		target	Consent2
	]
	edge
	[
		source	Request
		target	Consent3
	]
	edge
	[
		source	Request
		target	Consent4
	]
	edge
	[
		source	Consent2
		target	Sent1
	]
	edge
	[
		source	Consent3
		target	Sent1
	]
	edge
	[
		source	Consent4
		target	Sent1
	]
	edge
	[
		source	Consent1
		target	Sent2
	]
	edge
	[
		source	Consent2
		target	Sent2
	]
	edge
	[
		source	Consent3
		target	Sent2
	]
	edge
	[
		source	Consent4
		target	Sent2
	]
	edge
	[
		source	Notice1-su
		target	End
	]
	edge
	[
		source	Request
		target	Sent1
	]
	edge
	[
		source	Request
		target	Sent2
	]
	edge
	[
		source	Sent2
		target	End
	]
	edge
	[
		source	Sent1
		target	Notice1-su
	]
	edge
	[
		source	Sent1
		target	Notice2-su
	]
	edge
	[
		source	Sent2
		target	Notice1-su
	]
	edge
	[
		source	Sent2
		target	Notice2-su
	]
	edge
	[
		source	Notice2-su
		target	End
	]
	edge
	[
		source	Notice1-su
		target	Notice1-r
	]
	edge
	[
		source	Notice2-su
		target	Notice2-r
	]
	edge
	[
		source	Notice2-su
		target	Notice1-r
	]
	edge
	[
		source	Notice1-su
		target	Notice2-r
	]
	edge
	[
		source	Notice2-r
		target	End
	]
	edge
	[
		source	Notice1-r
		target	End
	]
]
