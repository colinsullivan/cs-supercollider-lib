SoftStepKtl : MIDIKtl {
  classvar <>verbose = false;

  *makeDefaults {

    defaults.put(this, (
      '1_pressure': '0_111',
      '2_pressure': '0_112',
      '3_pressure': '0_113',
      '4_pressure': '0_114',
      '5_pressure': '0_115',
      '6_pressure': '0_116',
      '7_pressure': '0_117',
      '8_pressure': '0_118',
      '9_pressure': '0_119',
      '0_pressure': '0_110'
    ));
  
  }
}
