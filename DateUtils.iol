type DateTime:void {
	.day:int
	.month:int
	.year:int
	.hour:int
	.minute:int
	.second:int
}

type DateTimeParsingRequest:string {
	.format:string
}

type DateTimeIsBeforeRequest:void {
	.before:DateTime
	.after:DateTime
}

interface DateUtilsInterface{
RequestResponse:
	parse( DateTimeParsingRequest )(DateTime),
	isBefore(DateTimeIsBeforeRequest)(bool)
}

outputPort DateUtils {
	Interfaces: DateUtilsInterface
}

embedded {
Java:
	"net.dateutils.DateUtils" in DateUtils
}