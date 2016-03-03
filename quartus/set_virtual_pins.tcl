load_package flow
package require cmdline

proc make_all_pins_virtual { src } {

    set src_list [format {%s%s} "--source=" $src]

    post_message $src_list

    execute_module -tool map -args "$src_list"

    set name_ids [get_names -filter * -node_type pin]

    foreach_in_collection name_id $name_ids {
        set pin_name [get_name_info -info full_path $name_id]
        post_message "Making VIRTUAL_PIN assignment to $pin_name"
        set_instance_assignment -to $pin_name -name VIRTUAL_PIN ON
    }
    export_assignments
}

set options {\
    { "project.arg" "" "Project name" } \
    { "src.arg" "" "src file" }
}
array set opts [::cmdline::getoptions quartus(args) $options]

project_open $opts(project)

make_all_pins_virtual $opts(src)

project_close
