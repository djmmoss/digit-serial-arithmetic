DESIGN = MSDFAddModule
DEVICE = xc7vx485tffg1761-2
XDC_FILE = ./constraints/${DESIGN}.xdc

# Make all runs to place & route
all : place_n_route

# bitstream : Creates device bitstream
bitstream : ./${DESIGN}.bit

# place_n_route: Stops after place and route for analysis prior to bitstream generation
place_n_route : ./${DESIGN}_route.dcp

# translate: Stops after full design elaboration and initial optimization for analysis and floorplanning prior to place and route step
translate : ./${DESIGN}_opt.dcp

# Following calls Tcl files for each desired portion of the Vivado run
# Design checkpoint files and bit file used for dependency management

./${DESIGN}.bit : ./run_vivado_place_n_route.tcl ./${DESIGN}_route.dcp
	vivado -mode batch -source run_vivado_bitstream.tcl -tclargs ${DESIGN}

./${DESIGN}_route.dcp : ./run_vivado_place_n_route.tcl ./${DESIGN}_opt.dcp
	vivado -mode batch -source run_vivado_place_n_route.tcl -tclargs ${DESIGN}

./${DESIGN}_opt.dcp : ./run_vivado_opt.tcl ${XDC_FILE}
	vivado -mode batch -source run_vivado_opt.tcl -tclargs ${DESIGN} ${DEVICE} ${XDC_FILE}

# Clean up all the files from the Vivado run
clean :
	rm -f *.jou *.log *.rpt *.dcp *.bit *.xml *.html

# Tar and compress all the files
tar :
	tar -zcvf ${DESIGN}.tar.gz *.jou *.log *.rpt *.dcp *.tcl Makefile 
