# Gathering TCL Args
set DESIGN [lindex $argv 0]
set DEVICE [lindex $argv 1]
set XDC_FILE [lindex $argv 2]

# Reading Verilog file
read_verilog [glob ./src/*.v]

# Adding Constraints
read_xdc ${XDC_FILE}

# Synth Design
synth_design -top ${DESIGN} -part ${DEVICE}

# Write Checkpoint
write_checkpoint -force ./${DESIGN}_post_synth.dcp

# Running Logic Optimization
opt_design -directive Explore

# Saving Run
write_checkpoint -force ./${DESIGN}_opt.dcp

# Creating opt reports
report_utilization -file ${DESIGN}_utilization_opt.rpt
report_timing_summary -file ${DESIGN}_timing_opt.rpt
report_io -file ${DESIGN}_io_opt.rpt
report_clock_interaction -file ${DESIGN}_clock_interaction_opt.rpt

exit
