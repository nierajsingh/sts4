/*******************************************************************************
 * Copyright (c) 2019 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
import { ContainerModule } from 'inversify';
import { ProgressService } from './progress-service';
import { MoveCursorService } from './move-cursor-service';
import {LanguageGrammarDefinitionContribution} from '@theia/monaco/lib/browser/textmate';
import {JavaPropertiesGrammarContribution} from './java-properties-grammar-contribution';
import {YamlGrammarContribution} from './yaml-grammar-contribution';

export default new ContainerModule(bind => {
    // add your contribution bindings here
    bind(ProgressService).toSelf().inSingletonScope();
    bind(MoveCursorService).toSelf().inSingletonScope();
    bind(LanguageGrammarDefinitionContribution).to(JavaPropertiesGrammarContribution).inSingletonScope();
    bind(LanguageGrammarDefinitionContribution).to(YamlGrammarContribution).inSingletonScope();
});