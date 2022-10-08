import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IFera } from 'app/shared/model/fera.model';
import { getEntities } from './fera.reducer';

export const Fera = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const feraList = useAppSelector(state => state.fera.entities);
  const loading = useAppSelector(state => state.fera.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="fera-heading" data-cy="FeraHeading">
        <Translate contentKey="gitPodReactSpringJpaApp.fera.home.title">Feras</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="gitPodReactSpringJpaApp.fera.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/fera/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="gitPodReactSpringJpaApp.fera.home.createLabel">Create new Fera</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {feraList && feraList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="gitPodReactSpringJpaApp.fera.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="gitPodReactSpringJpaApp.fera.feraName">Fera Name</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {feraList.map((fera, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/fera/${fera.id}`} color="link" size="sm">
                      {fera.id}
                    </Button>
                  </td>
                  <td>{fera.feraName}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/fera/${fera.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/fera/${fera.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/fera/${fera.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="gitPodReactSpringJpaApp.fera.home.notFound">No Feras found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Fera;
