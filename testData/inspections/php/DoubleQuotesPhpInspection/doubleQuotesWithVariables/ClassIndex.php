<?php
/**
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 */
namespace Magento\Test;

class ClassIndex implements InvalidInterface
{
    public function sample() {
        $variables = 'variables';
        return "String with $variables";
    }
}
?>