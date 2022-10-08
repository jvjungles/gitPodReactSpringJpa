import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Fera from './fera';
import FeraDetail from './fera-detail';
import FeraUpdate from './fera-update';
import FeraDeleteDialog from './fera-delete-dialog';

const FeraRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Fera />} />
    <Route path="new" element={<FeraUpdate />} />
    <Route path=":id">
      <Route index element={<FeraDetail />} />
      <Route path="edit" element={<FeraUpdate />} />
      <Route path="delete" element={<FeraDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FeraRoutes;
