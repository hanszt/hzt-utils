module fx.utils {

    requires javafx.base;
    requires hzt.utils.core;
    requires org.jetbrains.annotations;

    exports org.hzt.fx.utils;
    exports org.hzt.fx.utils.function;
    exports org.hzt.fx.utils.sequences;

    //required for surefire plugin to test files via maven
    opens org.hzt.fx.utils;
    opens org.hzt.fx.utils.sequences;
}
