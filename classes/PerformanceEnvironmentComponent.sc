/**
 *  @class  PerformanceEnvironmentComponent   A component of a performance
 *  environment that typically has a GUI window.
 **/
PerformanceEnvironmentComponent : Object {
  var <>origin,
    <>window,
    <>init_done_callback;

  *new {
    arg params;

    ^super.new.init(params);
  }

  init {
    arg params;

    ("  * Starting " + this.gui_window_title()).postln;
    ("----------------------------------------").postln;

    this.origin = params['origin'];
    this.init_done_callback = params['init_done_callback'];
  
  }

  init_gui {
    arg params;
   
    this.window = params['window'];



    this.window.bounds = this.window.bounds.moveToPoint(this.origin);
    this.window.name = this.gui_window_title();
  }

  init_external_controller_mappings {
    
  }

  /**
   *  Called to set title of GUI window.  Default is the name of the class
   *  of the component.
   **/
  gui_window_title {
    ^this.class.asString();
  }
}
