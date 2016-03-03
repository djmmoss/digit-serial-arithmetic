# Gathering TCL Arg
set DESIGN [lindex $argv 0]

open_checkpoint ./${DESIGN}_opt.dcp

# Placing Design
place_design -directive Explore
write_checkpoint -force ./${DESIGN}_place.dcp

# Phys
phys_opt_design -directive Explore

# Routing Design
route_design -directive Explore

place_design -post_place_opt
phys_opt_design -fanout_opt -retime -placement_opt -rewire -critical_cell_opt -shift_register_opt -dsp_register_opt -hold_fix
route_design -directive NoTimingRelaxation


# Saving Run
write_checkpoint -force ./${DESIGN}_route.dcp

# Creating route reports
report_utilization -file ${DESIGN}_utilization_route.rpt
report_io -file ${DESIGN}_io_route.rpt
report_clock_interaction -file ${DESIGN}_clock_interaction_route.rpt
report_timing_summary -file ${DESIGN}_timing_route.rpt
report_drc -file ${DESIGN}_drc_route.rpt

exit
