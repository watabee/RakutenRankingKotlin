#
# This file is generated via ./gradlew generatePodspec
#
Pod::Spec.new do |spec|
  spec.name                     = 'common'
  spec.version                  = '1.0.0'
  spec.homepage                 = 'https://github.com/watabee/RakutenRankingKotlin'
  spec.source                   = { :git => "Not Published", :tag => "Cocoapods/#{spec.name}/#{spec.version}" }
  spec.ios.deployment_target    = '11.0'
  spec.authors                  = 'watabee'
  spec.license                  = ''
  spec.summary                  = 'common'
  spec.ios.vendored_frameworks  = "build/#{spec.name}.framework"

  spec.prepare_command = <<-SCRIPT
    set -ev
    ../gradlew  -Pframework=#{spec.name}.framework initializeFramework --stacktrace
  SCRIPT

  spec.script_phases = [
    {
      :name => 'Build common',
      :shell_path => '/bin/sh',
      :script => <<-SCRIPT
        set -ev
        $PODS_TARGET_SRCROOT/../gradlew  -p "$PODS_TARGET_SRCROOT" "createIos${CONFIGURATION}Artifacts"
      SCRIPT
    }
  ]
end