package experiments.braindata;

public class Datasets {
    public static final String EEG_UEA_FingerMovements = "0_EEG_UEA_FingerMovements";
    public static final String MEG_BCICompIV_HandMovementDirectionS2 = "1_MEG_BCICompIV_HandMovementDirectionS2";
    public static final String MEG_BCICompIV_HandMovementDirectionS1 = "2_MEG_BCICompIV_HandMovementDirectionS1";
    public static final String EEG_UEA_SelfRegulationSCP2 = "3_EEG_UEA_SelfRegulationSCP2";
    public static final String EEG_UEA_SelfRegulationSCP1 = "4_EEG_UEA_SelfRegulationSCP1";
    public static final String EEG_UEA_MotorImagery = "5_EEG_UEA_MotorImagery";
    public static final String MEG_ICANN2011_MindReading = "6_MEG_ICANN2011_MindReading";
    public static final String EEG_UEA_FaceDetection = "7_EEG_UEA_FaceDetection";
    public static final String EEG_BCICompIII_P300SpellerSubjectA = "8_EEG_BCICompIII_P300SpellerSubjectA";
    public static final String EEG_BCICompIII_P300SpellerSubjectB = "9_EEG_BCICompIII_P300SpellerSubjectB";

    public static final String[] ALL_DATASETS = new String[] {
            EEG_UEA_FingerMovements,
            MEG_BCICompIV_HandMovementDirectionS2,
            MEG_BCICompIV_HandMovementDirectionS1,
            EEG_UEA_SelfRegulationSCP2,
            EEG_UEA_SelfRegulationSCP1,
            EEG_UEA_MotorImagery,
            MEG_ICANN2011_MindReading,
            EEG_UEA_FaceDetection,
            EEG_BCICompIII_P300SpellerSubjectA,
            EEG_BCICompIII_P300SpellerSubjectB
    };

    public static final String[] SMALL_DATASETS = new String[] {
            EEG_UEA_FingerMovements,
            MEG_BCICompIV_HandMovementDirectionS2,
            MEG_BCICompIV_HandMovementDirectionS1,
            EEG_UEA_SelfRegulationSCP2,
            EEG_UEA_SelfRegulationSCP1
    };

    public static final String[] BIG_DATASETS = new String[] {
            EEG_UEA_MotorImagery,
            MEG_ICANN2011_MindReading,
            EEG_UEA_FaceDetection,
            EEG_BCICompIII_P300SpellerSubjectA,
            EEG_BCICompIII_P300SpellerSubjectB
    };
}
