# Gathering TCL Arg
set DESIGN [lindex $argv 0]

open_checkpoint ./${DESIGN}_route.dcp

# Create bitstream
write_bitstream -force ${DESIGN}.bit

exit
