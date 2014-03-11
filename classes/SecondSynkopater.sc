SecondSynkopater : PerformanceEnvironmentComponent {
  var <>playTask,
    <>hardSineVoicer;

  init {
    arg params;

    super.init(params);
  }

  init_tracks {
    super.init_tracks();
  }

  init_patches {
    super.init_patches();

    this.hardSineVoicer = Voicer.new(
      2,
      Instr("cs.synths.HardSine")
    );

  }

  play_patches_on_tracks {
    super.play_patches_on_tracks();
    
    this.hardSineVoicer.target(this.outputChannel);
  }

  load_environment {
    var me = this;
    super.load_environment();


  }

  on_play {
    super.on_play();

  }

  init_gui {
    arg params;

    super.init_gui(params);


  }

  init_uc33_mappings {
    super.init_uc33_mappings();
  }

}
